package teamverpic.verpicbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.Notification;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.handler.StompHandler;
import teamverpic.verpicbackend.repository.NotificationRepository;
import teamverpic.verpicbackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class RelationshipService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final StompHandler stompHandler;

    public void relationshipRequest(Authentication authentication, Long receiverId) {
        Optional<User> currentLoginUser = userRepository.findByEmail(authentication.getName());
        System.out.println("currentLoginUser.get().getEmail() = " + currentLoginUser.get().getEmail());

        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new NullPointerException("존재하지 않는 유저에요.") );
        if(isRelationship(currentLoginUser.get(), receiver)) {
            System.out.println("이미 등록된 친구에요.");
        }
        else {
            if (stompHandler.getDoingSocketUserList().contains(receiverId.toString())) {
                System.out.println("메시지 수신 유저가 온라인이에요.");
                notificationService.alarmByMessage(currentLoginUser.get().getId(), receiverId, 1);
            } else {
                // 처리안했음 아직
                System.out.println("메시지 수신 유저가 오프라인이에요.");
            }
        }

    }


    public void relationshipAdd(Authentication authentication, String notificationId){
        Optional<User> currentLoginUser = userRepository.findByEmail(authentication.getName());

        Notification notification = notificationRepository.findBy_id(notificationId).
                orElseThrow(()-> new NullPointerException("해당 알림이 존재하지 않아요."));

        System.out.println("noti.getMessage() = " + notification.getMessage());

        if(currentLoginUser.get().getId() != notification.getReceiverId()){
            System.out.println("접근할 수 없는 알림이에요. (알림 수신자와 현재 로그인 유저가 달라요.)");
            return;
        }

        User sender = userRepository.findById(notification.getSenderId()).
                orElseThrow(()-> new NullPointerException("유저가 존재하지 않네요."));

        if(!isRelationship(currentLoginUser.get(), sender)) {
            currentLoginUser.get().setUserRelation(sender);
            sender.setUserRelation(currentLoginUser.get());
            userRepository.save(currentLoginUser.get());
            userRepository.save(sender);
            notificationRepository.deleteBy_id(notificationId);
            System.out.println("친구 등록이 완료되었습니다.");
        } else {
            System.out.println("이미 등록된 친구입니다.");
        }

    }

    public boolean isRelationship(User user1, User user2) {
        if(user1.getUserRelation().contains(user2))
            return true;
        else
            return false;
    }

}
