package teamverpic.verpicbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.handler.StompHandler;
import teamverpic.verpicbackend.repository.UserRepository;
import teamverpic.verpicbackend.service.NotificationService;
import teamverpic.verpicbackend.service.RelationshipService;

import java.util.Optional;

@RestController
@RequestMapping(value="/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    
    private final UserRepository userRepository;
    private final RelationshipService relationshipService;
    private final NotificationService notificationService;
    private final StompHandler stompHandler;


    @PostMapping("/{receiverId}")
    public void relationshipRequest(Authentication authentication, @PathVariable Long receiverId) {
        Optional<User> currentLoginUser = userRepository.findByEmail(authentication.getName());
        System.out.println("currentLoginUser.get().getEmail() = " + currentLoginUser.get().getEmail());

        Optional<User> receivedRequestUser = userRepository.findById(receiverId);
        receivedRequestUser.orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다.") );


        if(stompHandler.getDoingSocketUserList().contains(receiverId.toString())) {
            System.out.println("메시지 수신 유저가 온라인이에요.");
            notificationService.alarmByMessage(currentLoginUser.get().getId(), receiverId, 1);
        }else {
            System.out.println("메시지 수신 유저가 오프라인이에요.");
        }
    }

    @PostMapping("/accept/{senderId}")
    public void relationshipAccept(Authentication authentication, @PathVariable Long senderId) {
        // 친구 요청 보낸 사람의 id를 알 수 있는 방법?


        Optional<User> currentLoginUser = userRepository.findByEmail(authentication.getName());
        Optional<User> sendRequestUser = userRepository.findById(senderId);
        sendRequestUser.orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다.") );

        relationshipService.relationAdd(currentLoginUser.get(), sendRequestUser.get());


    }

}
