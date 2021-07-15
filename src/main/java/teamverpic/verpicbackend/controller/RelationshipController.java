package teamverpic.verpicbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.Notification;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.handler.StompHandler;
import teamverpic.verpicbackend.repository.NotificationRepository;
import teamverpic.verpicbackend.repository.UserRepository;
import teamverpic.verpicbackend.service.NotificationService;
import teamverpic.verpicbackend.service.RelationshipService;

import java.util.Optional;

@RestController
@RequestMapping(value="/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    private final RelationshipService relationshipService;

    // 친구 요청
    @PostMapping("/{receiverId}")
    public void relationshipRequest(Authentication authentication, @PathVariable Long receiverId) {
        relationshipService.relationshipRequest(authentication, receiverId);
    }

    // 친구 수락
    @PostMapping("/accept/{notificationId}")
    public void relationshipAccept(Authentication authentication, @PathVariable String notificationId) {
        relationshipService.relationshipAdd(authentication, notificationId);
    }

    // 친구 거절


    // 친구 삭제

}
