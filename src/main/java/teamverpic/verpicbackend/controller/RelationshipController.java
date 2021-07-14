package teamverpic.verpicbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.dto.MessageDto;
import teamverpic.verpicbackend.handler.StompHandler;
import teamverpic.verpicbackend.repository.UserRepository;
import teamverpic.verpicbackend.service.AlarmService;
import teamverpic.verpicbackend.service.RelationshipService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RelationshipController {
    
    private final UserRepository userRepository;
    private final RelationshipService relationshipService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final AlarmService alarmService;
    private final StompHandler stompHandler;


    @PostMapping("/relationship/{id}")
    public void relationshipRequest(Authentication authentication, @PathVariable Long id) {
        //null pointer exception, 친구수락 로직, response entity 등등 필요
        Optional<User> currentLoginUser = userRepository.findByEmail(authentication.getName());

        System.out.println("currentLoginUser.get().getEmail() = " + currentLoginUser.get().getEmail());

        Optional<User> receivedRequestUser = userRepository.findById(id);

        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(currentLoginUser.get().getFirstName() + currentLoginUser.get().getLastName() +"로 부터 친구 요청이 왔어요.");
        messageDto.setReceiver(receivedRequestUser.get());
        /*
        1. user 파라미터에 적절한 값이 들어갔는지,
        2. WebSocketConfig의 user destination 설정, principal handler 설정 등등 고려.
         */

        /*
        메시지를 받는 현재 유저가 세션에 존재하는지, 없으면 db 활용 해야 할듯.

         */
        if(stompHandler.getDoingSocketUserList().contains(id.toString())) {
            System.out.println("메시지 수신 유저가 온라인이에요.");
            alarmService.alarmByMessage(messageDto);
        }else {
            System.out.println("메시지 수신 유저가 오프라인이에요.");
        }
        System.out.println("receivedRequestUser.get().getEmail() = " + receivedRequestUser.get().getEmail());


    }

}
