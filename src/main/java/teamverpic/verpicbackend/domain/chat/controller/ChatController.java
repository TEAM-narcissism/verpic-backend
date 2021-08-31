package teamverpic.verpicbackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import teamverpic.verpicbackend.domain.chat.dto.ChatMessageDto;
import teamverpic.verpicbackend.domain.chat.service.ChatService;

import java.util.Date;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping(value = "/chat/enter")
    public void enter(Authentication authentication, @Payload ChatMessageDto message) {
        if (isAnonymousUser(authentication.getName())) {
            System.out.println("로그인 후 이용해주세요");
            return;
        }

        chatService.chatEnter(message, authentication.getName());
    }

    @MessageMapping(value = "/chat/message")
    public void send(Authentication authentication, @Payload ChatMessageDto message, StompHeaderAccessor accessor) {
        if (isAnonymousUser(authentication.getName())) {
            System.out.println("로그인 후 이용해주세요");
            return;
        }

        chatService.chatSend(message, authentication.getName(), accessor.getSessionId());
    }

    @MessageMapping(value = "/chat/load")
    public void load(Authentication authentication, @Payload ChatMessageDto message) {
        Long roomId = message.getRoomId();
        Date timeStamp = message.getTimeStamp();
        if (isAnonymousUser(authentication.getName())) {
            System.out.println("로그인 후 이용해주세요");
            return;
        }
        log.debug("roomId : {}, timeStamp : {}", roomId, timeStamp);
        chatService.chatLoad(roomId, timeStamp, authentication.getName());
    }

    private boolean isAnonymousUser(String username) {
        return username == "anonymousUser";
    }
}
