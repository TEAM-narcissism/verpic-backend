package teamverpic.verpicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import teamverpic.verpicbackend.dto.ChatMessageDto;
import teamverpic.verpicbackend.service.ChatService;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping(value = "/chat/enter")
    public void enter(Authentication authentication, ChatMessageDto message) {
        if (authentication.getName() == "anonymousUser") {
            System.out.println("로그인 후 이용해주세요");
            return;
        }

        chatService.chatEnter(message, authentication.getName());
    }

    @MessageMapping(value = "/chat/message")
    public void send(Authentication authentication, ChatMessageDto message) {
        if (authentication.getName() == "anonymousUser") {
            System.out.println("로그인 후 이용해주세요");
            return;
        }

        chatService.chatSend(message, authentication.getName());
    }
}
