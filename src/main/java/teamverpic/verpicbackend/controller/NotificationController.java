package teamverpic.verpicbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.service.NotificationService;


// 실험용 컨트롤러 입니다.
@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final NotificationService alarmService;

    @RequestMapping(value = "/notification", method = RequestMethod.GET)
    public String notifications() {
        return "stomp";
    }

    @MessageMapping("/hello")
    //@SendTo()
    public void message(/*@DestinationVariable("userId") Long userId*/) {
        System.out.println("여기.");
        messagingTemplate.convertAndSend("/sub/1", "실험용 메시지입니다.");
    }

    /* 2초 간격 자동 메시지 전송
    @Scheduled(fixedRate = 2000)
    public void fireGreeting() {
        messagingTemplate.convertAndSend("/sub/1", "유호성 ggly");
        System.out.println("스케쥴");
    }
    */

}

