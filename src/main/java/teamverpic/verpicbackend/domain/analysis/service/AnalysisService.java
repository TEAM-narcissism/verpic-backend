package teamverpic.verpicbackend.domain.analysis.service;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import teamverpic.verpicbackend.domain.analysis.dao.AudioRepository;
import teamverpic.verpicbackend.domain.analysis.dao.ScriptRepository;
import teamverpic.verpicbackend.domain.analysis.dao.SentenceRepository;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
import teamverpic.verpicbackend.domain.analysis.domain.Script;
import teamverpic.verpicbackend.domain.analysis.domain.Sentence;
import teamverpic.verpicbackend.domain.matching.dao.MatchRepository;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.exception.CustomAuthenticationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
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

    public Long saveAudio(MultipartFile multipartFile, String email, Language lang, Integer order, Long matchId) throws IOException, CustomAuthenticationException {
        String fileName = fileNameGen();
        String fileDir = saveFile(multipartFile, email, fileName);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));

        Match match = matchRepository.findById(matchId).orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        AudioFile audioFile = AudioFile.builder()
                .fileDir(fileDir)
                .lang(lang)
                .sessionOrder(order)
                .merged(false)
                .fileName(fileName)
                .build();
        user.addAudioFile(audioFile);
        match.addAudioFile(audioFile);
        return audioRepository.save(audioFile
        ).getAudioFileId();
    }

    public void soundToText(String email, Long matchId) throws IOException, ExecutionException, InterruptedException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        AudioFile audioFile = audioRepository.findByMatchAndUser(match, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        // 파일 업로드 세팅 및 업러드 (구글 스토리지)
        String projectId = "verpic-1628699741057";
        String bucketName = "verpic-speech-record";
        String objectName = audioFile.getFileName();
        String filePath = audioFile.getFileDir();
        uploadToGoogle(projectId, bucketName, objectName, filePath);

        // Sound to Text
        List<SpeechRecognitionResult> sttResult = asyncRecognizeGcs("gs://verpic-speech-record/" + objectName, audioFile.getLang());
        Script script = Script.builder().build();
        scriptRepository.save(script);
        audioFile.setScript(script);
        for (SpeechRecognitionResult result : sttResult) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            log.trace("Transcription : {}", alternative.getTranscript());

            int iterNum = 0;
            List<WordInfo> wordInfoList = alternative.getWordsList();
            String [] tempSentences = alternative.getTranscript().split("[.]");
            for (String tempSentence : tempSentences) {
                String noWhiteSpace = tempSentence.replace(" ", "");
                Sentence sentence = Sentence.builder()
                        .sentence(tempSentence + ".")
                        .startSecond((double)wordInfoList.get(iterNum).getStartTime().getSeconds()
                                + ((double)wordInfoList.get(iterNum).getStartTime().getNanos() / 100000000))
                        .build();
                script.addSentence(sentence);
                sentenceRepository.save(sentence);
                String tempString = "";
                for (; iterNum < alternative.getWordsCount(); iterNum++) {
                    if(tempString.equals(noWhiteSpace))
                        break;

                    tempString = tempString + wordInfoList.get(iterNum).getWord();
                }
            }
        }


        // 파일 삭제 (구글 스토리지)
        deleteFromGoogle(projectId, bucketName, objectName);
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
