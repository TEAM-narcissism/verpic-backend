package teamverpic.verpicbackend.domain.analysis.service;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamverpic.verpicbackend.domain.analysis.dao.AudioRepository;
import teamverpic.verpicbackend.domain.analysis.dao.ScriptRepository;
import teamverpic.verpicbackend.domain.analysis.dao.SentenceRepository;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
import teamverpic.verpicbackend.domain.analysis.domain.Script;
import teamverpic.verpicbackend.domain.analysis.domain.Sentence;
import teamverpic.verpicbackend.domain.analysis.dto.ScriptDto;
import teamverpic.verpicbackend.domain.analysis.dto.SentenceDto;
import teamverpic.verpicbackend.domain.matching.dao.MatchRepository;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.exception.CustomAuthenticationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;

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
            throws IOException, CustomAuthenticationException, InterruptedException, ExecutionException {
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
        scriptRepository.save(script);
        audioFile.setScript(script);
        for (SpeechRecognitionResult result : sttResult) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            totalWordCount += alternative.getWordsCount();
            log.trace("Transcription : {}", alternative.getTranscript());

            int iterNum = 0;
            List<WordInfo> wordInfoList = alternative.getWordsList();
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

        script.setWpm((double)totalWordCount / totalTime * 60);
        scriptRepository.save(script);
        // 파일 삭제 (구글 스토리지)
        deleteFromGoogle(projectId, bucketName, objectName);
        return 0L;
    }

    public ScriptDto getMatchScript(String email, Long matchId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        List<AudioFile> audioFileList = match.getAudioFileList();
        List<SentenceDto> matchScript = new ArrayList<SentenceDto>();
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
        }
        System.out.println("matchScript.size() = " + matchScript.size());
        Collections.sort(matchScript);
        return new ScriptDto(
                user.getId(),
                matchScript
        );
    }

    public String saveFile(MultipartFile multipartFile, String userName, String fileName) throws IOException {

        File dir = new File("database/audiofile/" + userName + "/");
        if(!dir.exists())
            dir.mkdir();
        String fileDir = "database/audiofile/" + userName + "/" + fileName;
        File file = new File(fileDir);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return fileDir;
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
                            .setSampleRateHertz(44100)
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
