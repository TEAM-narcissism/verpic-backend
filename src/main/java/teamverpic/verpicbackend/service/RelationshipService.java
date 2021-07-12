package teamverpic.verpicbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class RelationshipService {

    private final UserRepository userRepository;

    public void relationAdd(Optional<User> user1, Optional<User> user2){
        if(!isRelation(user1, user2)) {
            user1.get().setUserRelation(user2.get());
            user2.get().setUserRelation(user1.get());
            userRepository.save(user1.get());
            userRepository.save(user2.get());
            System.out.println("친구 등록이 완료되었습니다.");
        } else {
            System.out.println("이미 등록된 친구입니다.");
        }


    }

    public boolean isRelation(Optional<User> user1, Optional<User> user2) {
        if(user1.get().getUserRelation().contains(user2.get()))
            return true;
        else
            return false;
    }

}
