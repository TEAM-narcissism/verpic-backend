package teamverpic.verpicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import teamverpic.verpicbackend.domain.ChatRoom;
import teamverpic.verpicbackend.dto.ChatRoomDto;
import teamverpic.verpicbackend.service.ChatRoomService;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @RequestMapping(value = "/chatroom", method = RequestMethod.GET)
    public String chatRoomJoin(String receiverName, Model model) {
        // 요청한 유저 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == "anonymousUser") {
            System.out.println("로그인 후 이용해주세요");
            return "index";
        }

        String userName = authentication.getName();
        ChatRoom room = chatRoomService.chatRoomJoin(userName, receiverName);

        model.addAttribute("sender", userName);
        model.addAttribute("receiver", receiverName);
        model.addAttribute("roomId", room.getRoomId());
        System.out.println("room = " + room.getRoomId());
        return "chatroom";
    }
}
