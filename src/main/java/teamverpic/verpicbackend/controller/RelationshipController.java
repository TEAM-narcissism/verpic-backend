package teamverpic.verpicbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.customexception.CustomNullPointerException;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.dto.HttpResponseDto;
import teamverpic.verpicbackend.repository.UserRepository;
import teamverpic.verpicbackend.service.RelationshipService;

import java.nio.charset.Charset;

@RestController
@RequestMapping(value="/relationship")
@RequiredArgsConstructor
public class RelationshipController {
    private final RelationshipService relationshipService;
    private final UserRepository userRepository;



    // 친구 요청
    @PostMapping("/{receiverId}")
    public String relationshipRequest(Authentication authentication, @PathVariable Long receiverId) {
        String userEmail = authentication.getName();
        relationshipService.relationshipRequest(userEmail, receiverId);
        return "{ id: " +receiverId + " } 님에게 친구 요청을 보냈어요.";
    }

    // 친구 수락
    @PostMapping("/accept/{notificationId}")
    public void relationshipAccept(Authentication authentication, @PathVariable String notificationId) {
        relationshipService.relationshipAdd(authentication, notificationId);
    }

    // 친구 삭제
    @DeleteMapping("/{friendId}")
    public void relationshipDelete(Authentication authentication, @PathVariable Long friendId){
        relationshipService.relationshipRemove(authentication, friendId);
    }

    // 친구 거절
    @DeleteMapping("/reject/{notificationId}")
    public void relationshipReject(Authentication authentication, @PathVariable String notificationId) {
        relationshipService.relationshipReject(authentication, notificationId);
    }




}
