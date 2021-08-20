package teamverpic.verpicbackend.domain.analysis.service;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import teamverpic.verpicbackend.domain.analysis.dao.AudioRepository;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
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

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AudioRepository audioRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

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

    public void soundToText(String email, Long matchId) throws IOException, ExecutionException, InterruptedException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        AudioFile audioFile = audioRepository.findByMatchAndUser(match, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다. matchId=" + matchId));

        String projectId = "verpic-1628699741057";
        String bucketName = "verpic-speech-record";
        String objectName = audioFile.getFileName();
        String filePath = audioFile.getFileDir();
        uploadToGoogle(projectId, bucketName, objectName, filePath);

        asyncRecognizeGcs("gs://verpic-speech-record/" + objectName);

        deleteFromGoogle(projectId, bucketName, objectName);
    }

    public void uploadToGoogle(String projectId, String bucketName, String objectName, String filePath) throws  IOException{
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(
                        GoogleCredentials
                                .fromStream(
                                        ResourceUtils
                                                .getURL("/Users/yk0318ha/Downloads/verpic-1628699741057-7298f7a6fc47.json")
                                                .openStream()))
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

    public void asyncRecognizeGcs(String gcsUri) throws IOException, ExecutionException, InterruptedException {
        // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
        try (SpeechClient speech = SpeechClient.create()) {

            // Configure remote file request for FLAC
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("ko-KR")
                            .setSampleRateHertz(44100)
                            .setAudioChannelCount(1)
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

            // Use non-blocking call for getting file transcription
            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                    speech.longRunningRecognizeAsync(config, audio);
            while (!response.isDone()) {
                System.out.println("Waiting for response...");
                Thread.sleep(10000);
            }

            List<SpeechRecognitionResult> results = response.get().getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s\n", alternative.getTranscript());
            }
        }
    }

    public static void longRecognitionSpeech(String filePath) throws IOException, ExecutionException, InterruptedException {
        SpeechClient speech = SpeechClient.create();


            // 오디오 파일에 대한 설정부분
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(44100)
                .setLanguageCode("ko-KR")
                .build();

        RecognitionAudio audio = getRecognitionAudio(filePath);
        OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                speech.longRunningRecognizeAsync(config, audio);
        while (!response.isDone()) {
            System.out.println("Waiting for response...");
            Thread.sleep(10000);
        }

        List<SpeechRecognitionResult> results = response.get().getResultsList();

        for (SpeechRecognitionResult result: results) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            System.out.printf("Transcription: %s\n",alternative.getTranscript());
        }
        speech.close();
    }

    public static RecognitionAudio getRecognitionAudio(String filePath) throws IOException {
        RecognitionAudio recognitionAudio;

        recognitionAudio = RecognitionAudio.newBuilder()
                .setUri(filePath)
                .build();

        return recognitionAudio;
    }
}
