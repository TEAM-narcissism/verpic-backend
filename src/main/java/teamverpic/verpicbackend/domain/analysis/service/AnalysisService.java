package teamverpic.verpicbackend.domain.analysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamverpic.verpicbackend.domain.analysis.dao.AudioRepository;
import teamverpic.verpicbackend.domain.analysis.dao.ScriptRepository;
import teamverpic.verpicbackend.domain.analysis.dao.SentenceRepository;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
import teamverpic.verpicbackend.domain.analysis.domain.Script;
import teamverpic.verpicbackend.domain.analysis.domain.Sentence;
import teamverpic.verpicbackend.domain.analysis.dto.AnalysisDto;
import teamverpic.verpicbackend.domain.analysis.dto.MostUsedWordDto;
import teamverpic.verpicbackend.domain.analysis.dto.ScriptDto;
import teamverpic.verpicbackend.domain.analysis.dto.SentenceDto;
import teamverpic.verpicbackend.domain.matching.dao.MatchRepository;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.exception.CustomAuthenticationException;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AudioRepository audioRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ScriptRepository scriptRepository;
    private final SentenceRepository sentenceRepository;

    public Long saveAudioAndStt(MultipartFile multipartFile, String email, Language lang, Integer order, Long matchId)
            throws IOException, InterruptedException, ExecutionException {
        String fileName = fileNameGen();
        String fileDir = saveFile(multipartFile, email, fileName);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        AudioFile audioFile = AudioFile.builder()
                .fileDir(fileDir)
                .lang(lang)
                .sessionOrder(order)
                .merged(false)
                .fileName(fileName)
                .build();
        user.addAudioFile(audioFile);
        match.addAudioFile(audioFile);
        audioRepository.save(audioFile);

        // 파일 업로드 세팅 및 업러드 (구글 스토리지)
        String projectId = "verpic-1628699741057";
        String bucketName = "verpic-speech-record";
        String objectName = audioFile.getFileName();
        String filePath = audioFile.getFileDir();
        uploadToGoogle(projectId, bucketName, objectName, filePath);

        // Sound to Text
        List<SpeechRecognitionResult> sttResult = asyncRecognizeGcs("gs://verpic-speech-record/" + objectName, audioFile.getLang());
        Script script = Script.builder().build();
        double totalTime = 0.0;
        int totalWordCount = 0;
        String totalScript = "";
        List<WordInfo> totalWordInfoList = new ArrayList<>();
        scriptRepository.save(script);
        audioFile.setScript(script);

        for (SpeechRecognitionResult result : sttResult) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            totalWordCount += alternative.getWordsCount();
            totalScript = totalScript + alternative.getTranscript() + " ";

            int iterNum = 0;
            List<WordInfo> wordInfoList = alternative.getWordsList();
            totalWordInfoList = Stream.concat(totalWordInfoList.stream(), wordInfoList.stream()).collect(Collectors.toList());
            String [] tempSentences = alternative.getTranscript().split("[.]");
            for (String tempSentence : tempSentences) {
                String noWhiteSpace = tempSentence.replace(" ", "");
                Sentence sentence = Sentence.builder()
                        .sentence(tempSentence.trim() + ".")
                        .startSecond((double)wordInfoList.get(iterNum).getStartTime().getSeconds()
                                + ((double)wordInfoList.get(iterNum).getStartTime().getNanos() / 1000000000))
                        .build();
                script.addSentence(sentence);

                String tempString = "";
                for (; iterNum < alternative.getWordsCount(); iterNum++) {
                    if(tempString.equals(noWhiteSpace))
                        break;
                    tempString = tempString + wordInfoList.get(iterNum).getWord();
                }
                sentence.setEndSecond((double)wordInfoList.get(iterNum - 1).getStartTime().getSeconds()
                        + ((double)wordInfoList.get(iterNum - 1).getStartTime().getNanos() / 1000000000));
                sentenceRepository.save(sentence);
                totalTime += (double)wordInfoList.get(iterNum - 1).getStartTime().getSeconds()
                        + ((double)wordInfoList.get(iterNum - 1).getStartTime().getNanos() / 1000000000);
            }
        }
        // WPM
        script.setWpm((double)totalWordCount / totalTime * 60);

        // Most Used Word
        JSONArray wordObjects = requestMostUsedWord(totalScript, lang);
        if (wordObjects.length() >= 1) {
            script.setMuwRankOne(wordObjects.getJSONObject(0).getString("word"));
            script.setMuwRankOneFreq(wordObjects.getJSONObject(0).getInt("count"));
        }
        if (wordObjects.length() >= 2) {
            script.setMuwRankTwo(wordObjects.getJSONObject(1).getString("word"));
            script.setMuwRankTwoFreq(wordObjects.getJSONObject(1).getInt("count"));
        }
        if (wordObjects.length() >= 3) {
            script.setMuwRankThree(wordObjects.getJSONObject(2).getString("word"));
            script.setMuwRankThreeFreq(wordObjects.getJSONObject(2).getInt("count"));
        }
        if (wordObjects.length() >= 4) {
            script.setMuwRankFour(wordObjects.getJSONObject(3).getString("word"));
            script.setMuwRankFourFreq(wordObjects.getJSONObject(3).getInt("count"));
        }
        if (wordObjects.length() >= 5) {
            script.setMuwRankFive(wordObjects.getJSONObject(4).getString("word"));
            script.setMuwRankFiveFreq(wordObjects.getJSONObject(4).getInt("count"));
        }

        scriptRepository.save(script);

        // 파일 삭제 (구글 스토리지)
        deleteFromGoogle(projectId, bucketName, objectName);
        return 0L;
    }

    public JSONArray requestMostUsedWord(String script, Language lang)
            throws IllegalStateException, IOException {
        String language;
        if (lang == Language.ENG)
            language = "en";
        else
            language = "ko";
        URL url = new URL("http://localhost:8000/api/mostused");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        String inputLine = null;
        StringBuffer outResult = new StringBuffer();

        HashMap<String, Object> resultMap = new HashMap();
        ObjectMapper mapper = new ObjectMapper();
        resultMap.put("script", script);
        resultMap.put("rank", 5);
        resultMap.put("lang", language);
        String jsonValue = mapper.writeValueAsString(resultMap);

        OutputStream os = conn.getOutputStream();
        os.write(jsonValue.getBytes("UTF-8"));
        os.flush();
        // 리턴된 결과 읽기
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        while ((inputLine = in.readLine()) != null) {
            outResult.append(inputLine);
        }
        conn.disconnect();

        String returnString = outResult.toString();
        JSONObject jsonObject = new JSONObject(returnString);
        return jsonObject.getJSONArray("data");
    }

    public ScriptDto getMatchScriptAndAnalysis(String email, Long matchId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        List<AudioFile> audioFileList = match.getAudioFileList();
        List<SentenceDto> matchScript = new ArrayList<>();
        List<AnalysisDto> analysisList = new ArrayList<>();
        for (AudioFile audioFile : audioFileList) {
            Script script = audioFile.getScript();
            List<Sentence> sentenceList = script.getSentenceList();
            for (Sentence sentence : sentenceList) {
                matchScript.add(new SentenceDto(
                        audioFile.getUser().getFirstName() + audioFile.getUser().getLastName(),
                        sentence.getSentence(),
                        audioFile.getUser().getId(),
                        audioFile.getSessionOrder(),
                        sentence.getStartSecond(),
                        sentence.getEndSecond()
                ));
            }

            // Get User Analysis
            if (audioFile.getUser().getEmail().equals(email)) {
                System.out.println("Asdf");
                List<MostUsedWordDto> mostUsedWordList = new ArrayList<>();

                mostUsedWordList.add(new MostUsedWordDto(
                        script.getMuwRankOne(),
                        script.getMuwRankOneFreq()
                ));
                mostUsedWordList.add(new MostUsedWordDto(
                        script.getMuwRankTwo(),
                        script.getMuwRankTwoFreq()
                ));
                mostUsedWordList.add(new MostUsedWordDto(
                        script.getMuwRankThree(),
                        script.getMuwRankThreeFreq()
                ));
                mostUsedWordList.add(new MostUsedWordDto(
                        script.getMuwRankFour(),
                        script.getMuwRankFourFreq()
                ));
                mostUsedWordList.add(new MostUsedWordDto(
                        script.getMuwRankFive(),
                        script.getMuwRankFiveFreq()
                ));

                analysisList.add(new AnalysisDto(
                        mostUsedWordList,
                        script.getWpm(),
                        audioFile.getSessionOrder()
                ));

            }
        }
        Collections.sort(matchScript);

        return new ScriptDto(
                user.getId(),
                matchScript,
                analysisList
        );
    }

    public String saveFile(MultipartFile multipartFile, String userName, String fileName) throws IOException {

        String filePath = System.getProperty("user.dir")+ "database/audiofile/" + userName + "/";
        System.out.println(System.getProperty("user.dir"));
        File dir = new File(filePath);
        if(!dir.exists())
            dir.mkdirs();

        filePath= filePath + fileName;

        File file = new File(filePath);
        multipartFile.transferTo(file);
        return filePath;

    }

    public String fileNameGen() {
        String alphabet =
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        int n = alphabet.length();
        String generatedString = "";
        Random r = new Random();

        for (int i = 0; i < 25; i++) // 12
            generatedString = generatedString + alphabet.charAt(r.nextInt(n));
        return generatedString + ".wav";
    }

    public void uploadToGoogle(String projectId, String bucketName, String objectName, String filePath) throws  IOException{
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
    }

    public void deleteFromGoogle(String projectId, String bucketName, String objectName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        storage.delete(bucketName, objectName);
    }

    public List<SpeechRecognitionResult> asyncRecognizeGcs(String gcsUri, Language lang) throws IOException, ExecutionException, InterruptedException {
        List<SpeechRecognitionResult> results = null;

        // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
        try (SpeechClient speech = SpeechClient.create()) {
            String language = "";
            if(lang == Language.KOR)
                language = "ko-KR";
            else
                language = "en-EN";
            // Configure remote file request for FLA
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode(language)
                            .setSampleRateHertz(48000)
                            .setAudioChannelCount(1)
                            .setEnableAutomaticPunctuation(true)
                            .setEnableWordTimeOffsets(true)
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

            // Use non-blocking call for getting file transcription
            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                    speech.longRunningRecognizeAsync(config, audio);
            while (!response.isDone()) {
                System.out.println("Waiting for Google Cloud to Response...");
                Thread.sleep(10000);
            }

            results = response.get().getResultsList();
        }
        return results;
    }
}
