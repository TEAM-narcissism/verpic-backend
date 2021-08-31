package teamverpic.verpicbackend.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import teamverpic.verpicbackend.domain.chat.domain.ChatRoom;
import teamverpic.verpicbackend.domain.chat.service.ChatRoomService;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping(value = "/chatroom/{receiverName}")
    public String chatRoomJoin(@PathVariable String receiverName, Model model) {
        // 요청한 유저 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == "anonymousUser") {
            System.out.println("로그인 후 이용해주세요");
            return "index";
        }
        else if (authentication.getName() == receiverName) {
            System.out.println("자신과의 채팅방은 없습니다.");
        }
        ChatRoom room;
        String userName = authentication.getName();
        try {
            room = chatRoomService.chatRoomJoin(userName, receiverName);
        } catch (NullPointerException e) {
            System.out.println(userName + "과(와) " + receiverName + "가(이) 서로 친구이지 않습니다");
            return "index";
        }
        model.addAttribute("sender", userName);
        model.addAttribute("receiver", receiverName);
        model.addAttribute("roomId", room.getRoomId());
        System.out.println("room = " + room.getRoomId());
        return "chatroom";
    }
}
