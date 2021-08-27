//package teamverpic.verpicbackend.domain.stt;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class SttController {
//
//    private final SttService sttService;
//
//    @GetMapping("/stt")
//    public void example() throws Exception {
//        try{
//            System.out.println("SttController.stt");
//            sttService.example();
//        }catch (Exception e) {
//            System.out.println("SttController.stt fail");
//            e.printStackTrace();
//        }
//    }
//
//    @GetMapping("/longstt")
//    public void asyncRecognizeGcs() throws Exception{
//        try{
//            String gcsUri = "gs://verpic-speech-record/녹음.flac";
//            sttService.asyncRecognizeGcs(gcsUri);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
////    @GetMapping("/streaming")
////    public void streaming() throws Exception {
////        try{
////            sttService.streamingMicRecognize();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//}
