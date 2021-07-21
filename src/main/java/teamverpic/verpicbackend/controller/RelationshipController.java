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

    @ExceptionHandler
    public ResponseEntity<HttpResponseDto> customNullPointerExceptionHandler(CustomNullPointerException e){
        return new ResponseEntity<>(e.getResponseDto(), e.getHttpHeaders(), e.getResponseDto().getHttpStatus());
    }


    // 친구 요청
    @PostMapping("/{receiverId}")
    public ResponseEntity<HttpResponseDto> relationshipRequest(Authentication authentication, @PathVariable Long receiverId) {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        HttpResponseDto responseDto = new HttpResponseDto();

        responseDto.setHttpStatus(HttpStatus.BAD_REQUEST);
        responseDto.setMessage("현재 로그인한 유저를 찾을 수 없어요.");

        HttpResponseDto finalResponseDto = responseDto;
        User currentLoginUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(()->
                        new CustomNullPointerException(finalResponseDto, headers));

        responseDto.setMessage("친구 요청을 받을 유저를 찾을 수 없어요.");
        HttpResponseDto finalResponseDto1 = responseDto;
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(()->
                        new CustomNullPointerException(finalResponseDto1, headers));

        System.out.println();
        System.out.println("####################### 친구 요청 api 실행 #######################");
        System.out.println("currentLoginUser.getEmail() = " + currentLoginUser.getEmail());
        System.out.println("################################################################");
        System.out.println();

        responseDto = relationshipService.relationshipRequest(currentLoginUser, receiver);
        if(responseDto.getHttpStatus() != HttpStatus.CREATED) {
            return new ResponseEntity<>(responseDto, headers, responseDto.getHttpStatus());
        } else {
            responseDto.setMessage(receiver.getFirstName() + receiver.getLastName() + " 님에게 친구 요청을 보냈어요.");
            responseDto.setHttpStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(responseDto, headers, HttpStatus.CREATED);
        }
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
