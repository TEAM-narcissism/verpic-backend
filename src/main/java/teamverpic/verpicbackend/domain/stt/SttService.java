//package teamverpic.verpicbackend.domain.stt;
//
//// Imports the Google Cloud client library
//import com.google.api.gax.longrunning.OperationFuture;
//import com.google.api.gax.rpc.ClientStream;
//import com.google.api.gax.rpc.ResponseObserver;
//import com.google.api.gax.rpc.StreamController;
//import com.google.cloud.speech.v1.*;
//import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
//import com.google.protobuf.ByteString;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.sound.sampled.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class SttService {
//
//    /** Demonstrates using the Speech API to transcribe an audio file. */
//    public void example() throws Exception {
//        // Instantiates a client
//        try (SpeechClient speechClient = SpeechClient.create()) {
//
//            // The path to the audio file to transcribe
//            String gcsUri = "gs://cloud-samples-data/speech/brooklyn_bridge.raw";
//
//            // Builds the sync recognize request
//            RecognitionConfig config =
//                    RecognitionConfig.newBuilder()
//                            .setEncoding(AudioEncoding.LINEAR16)
//                            .setSampleRateHertz(16000)
//                            .setLanguageCode("en-US")
//                            .build();
//            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
//
//            // Performs speech recognition on the audio file
//            RecognizeResponse response = speechClient.recognize(config, audio);
//            List<SpeechRecognitionResult> results = response.getResultsList();
//
//            for (SpeechRecognitionResult result : results) {
//                // There can be several alternative transcripts for a given chunk of speech. Just use the
//                // first (most likely) one here.
//                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//                System.out.printf("Transcription: %s%n", alternative.getTranscript());
//            }
//        }
//    }
//
//    /**
//     * Performs non-blocking speech recognition on remote FLAC file and prints the transcription.
//     *
//     * @param gcsUri the path to the remote LINEAR16 audio file to transcribe.
//     */
//    public void asyncRecognizeGcs(String gcsUri) throws Exception {
//        // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
//        try (SpeechClient speech = SpeechClient.create()) {
//
//            // Configure remote file request for FLAC
//            RecognitionConfig config =
//                    RecognitionConfig.newBuilder()
//                            .setEncoding(AudioEncoding.FLAC)
//                            .setLanguageCode("ko-KR")
//                            .setSampleRateHertz(48000)
//                            .setAudioChannelCount(2)
//                            .build();
//            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
//
//            // Use non-blocking call for getting file transcription
//            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
//                    speech.longRunningRecognizeAsync(config, audio);
//            while (!response.isDone()) {
//                System.out.println("Waiting for response...");
//                Thread.sleep(10000);
//            }
//
//            List<SpeechRecognitionResult> results = response.get().getResultsList();
//
//            for (SpeechRecognitionResult result : results) {
//                // There can be several alternative transcripts for a given chunk of speech. Just use the
//                // first (most likely) one here.
//                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//                System.out.printf("Transcription: %s\n", alternative.getTranscript());
//            }
//        }
//    }
//
//    /** Performs microphone streaming speech recognition with a duration of 1 minute. */
//    public void streamingMicRecognize() throws Exception {
//
//        ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
//        try (SpeechClient client = SpeechClient.create()) {
//
//            responseObserver =
//                    new ResponseObserver<StreamingRecognizeResponse>() {
//                        ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();
//
//                        public void onStart(StreamController controller) {}
//
//                        public void onResponse(StreamingRecognizeResponse response) {
//                            responses.add(response);
//                        }
//
//                        public void onComplete() {
//                            for (StreamingRecognizeResponse response : responses) {
//                                StreamingRecognitionResult result = response.getResultsList().get(0);
//                                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//                                System.out.printf("Transcript : %s\n", alternative.getTranscript());
//                            }
//                        }
//
//                        public void onError(Throwable t) {
//                            System.out.println(t);
//                        }
//                    };
//
//            ClientStream<StreamingRecognizeRequest> clientStream =
//                    client.streamingRecognizeCallable().splitCall(responseObserver);
//
//            RecognitionConfig recognitionConfig =
//                    RecognitionConfig.newBuilder()
//                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
//                            .setLanguageCode("en-US")
//                            .setSampleRateHertz(16000)
//                            .build();
//            StreamingRecognitionConfig streamingRecognitionConfig =
//                    StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).build();
//
//            StreamingRecognizeRequest request =
//                    StreamingRecognizeRequest.newBuilder()
//                            .setStreamingConfig(streamingRecognitionConfig)
//                            .build(); // The first request in a streaming call has to be a config
//
//            clientStream.send(request);
//            // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
//            // bigEndian: false
//            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
//            DataLine.Info targetInfo =
//                    new DataLine.Info(
//                            TargetDataLine.class,
//                            audioFormat); // Set the system information to read from the microphone audio stream
//
//            if (!AudioSystem.isLineSupported(targetInfo)) {
//                System.out.println("Microphone not supported");
//                System.exit(0);
//            }
//            // Target data line captures the audio stream the microphone produces.
//            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
//            targetDataLine.open(audioFormat);
//            targetDataLine.start();
//            System.out.println("Start speaking");
//            long startTime = System.currentTimeMillis();
//            // Audio Input Stream
//            AudioInputStream audio = new AudioInputStream(targetDataLine);
//            while (true) {
//                long estimatedTime = System.currentTimeMillis() - startTime;
//                byte[] data = new byte[6400];
//                audio.read(data);
//                if (estimatedTime > 60000) { // 60 seconds
//                    System.out.println("Stop speaking.");
//                    targetDataLine.stop();
//                    targetDataLine.close();
//                    break;
//                }
//                request =
//                        StreamingRecognizeRequest.newBuilder()
//                                .setAudioContent(ByteString.copyFrom(data))
//                                .build();
//                clientStream.send(request);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        responseObserver.onComplete();
//    }
//}