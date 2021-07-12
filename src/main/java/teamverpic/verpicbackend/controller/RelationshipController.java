package teamverpic.verpicbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.repository.UserRepository;
import teamverpic.verpicbackend.service.RelationshipService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RelationshipController {
    
    private final UserRepository userRepository;
    private final RelationshipService relationshipService;

    @PostMapping("/relationship/{id}")
    public void create(Authentication authentication, @PathVariable Long id) {
        //null pointer exception, 친구수락 로직, response entity 등등 필요
        Optional<User> currentLoginUser = userRepository.findByEmail(authentication.getName());

        System.out.println("currentLoginUser.get().getEmail() = " + currentLoginUser.get().getEmail());

        Optional<User> receivedRequestUser = userRepository.findById(id);

        System.out.println("receivedRequestUser.get().getEmail() = " + receivedRequestUser.get().getEmail());
        relationshipService.relationAdd(currentLoginUser, receivedRequestUser);

    }

}
