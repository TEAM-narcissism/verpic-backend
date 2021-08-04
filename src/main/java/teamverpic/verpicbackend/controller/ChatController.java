package teamverpic.verpicbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Test;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.dto.ChatMessageDto;
import teamverpic.verpicbackend.service.ChatService;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/chat/enter")
    public void enter(Authentication authentication, ChatMessageDto message) {
        if (authentication.getName() == "anonymousUser") {
            System.out.println("로그인 후 이용해주세요");
            return;
        }

        chatService.chatEnter(message, authentication.getName());
    }

    @MessageMapping(value = "/chat/message")
    public void send(Authentication authentication, ChatMessageDto message, StompHeaderAccessor accessor) {
        if (authentication.getName() == "anonymousUser") {
            System.out.println("로그인 후 이용해주세요");
            return;
        }

        chatService.chatSend(message, authentication.getName(), accessor.getSessionId());
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();

    }
}
