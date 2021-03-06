package teamverpic.verpicbackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.domain.chat.dto.VideoChatMessageDto;
import teamverpic.verpicbackend.domain.chat.service.VideoChatService;
import teamverpic.verpicbackend.domain.user.domain.User;

import java.security.Principal;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class VideoChatController {

    private final VideoChatService videoChatService;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/videochat/enter")
    public void enter(@Payload VideoChatMessageDto messageDto, StompHeaderAccessor accessor, Principal principal) {
        Authentication authentication = getAuthentication(accessor);
        User user = (User) authentication.getPrincipal();
        videoChatService.enter(messageDto, user, principal);
    }

    @MessageMapping("/videochat/message")
    public void send(@Payload VideoChatMessageDto messageDto, StompHeaderAccessor accessor) {
        Authentication authentication = getAuthentication(accessor);
        User user = (User) authentication.getPrincipal();
        videoChatService.send(messageDto, user);
    }

    @GetMapping("/videochat/{matchId}")
    public String tempJoin(@PathVariable Long matchId, Authentication authentication, Model model) {
        return "chatroom_video";
    }

    private Authentication getAuthentication(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        return jwtTokenProvider.getAuthentication(token);
    }
}
