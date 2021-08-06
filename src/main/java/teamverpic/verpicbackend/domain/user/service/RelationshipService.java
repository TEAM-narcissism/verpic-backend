package teamverpic.verpicbackend.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.user.domain.Notification;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.studyroom.handler.StompCommandListener;
import teamverpic.verpicbackend.domain.user.dao.NotificationRepository;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;

@RequiredArgsConstructor
@Service
public class RelationshipService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final StompCommandListener stompCommandListener;
    private final UserService userService;

    public void relationshipRequest(String userEmail, Long receiverId) {

        User currentLoginUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저에요."));

        User receiver =  userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저에요."));

        if(isRelationship(currentLoginUser, receiver)) {
            System.out.println("이미 등록된 친구에요.");
            throw new IllegalArgumentException("이미 등록된 친구에요.");

        }
        else if(currentLoginUser.getId() == receiver.getId()){
            System.out.println("본인과는 친구가 될 수 없어요.");
            throw new IllegalArgumentException("본인과는 친구가 될 수 없어요.");
        }
        else {
            if (stompCommandListener.getSocketParticipateUserList().contains(receiver.getEmail())) {
                System.out.println("메시지 수신 유저가 온라인이에요.");
                notificationService.alarmByMessage(currentLoginUser.getId(), receiver.getId(), 1);
            } else {
                // 처리안했음 아직
                System.out.println("메시지 수신 유저가 오프라인이에요.");

            }
        }


    }

    public void relationshipAdd(Authentication authentication, String notificationId){
        User currentLoginUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저에요."));

        Notification notification = notificationRepository.findBy_id(notificationId).
                orElseThrow(()-> new NullPointerException("해당 알림이 존재하지 않아요."));

        System.out.println("noti.getMessage() = " + notification.getMessage());

        if(currentLoginUser.getId() != notification.getReceiverId()){
            System.out.println("접근할 수 없는 알림이에요. (알림 수신자와 현재 로그인 유저가 달라요.)");
            return;
        }

        User sender = userRepository.findById(notification.getSenderId()).
                orElseThrow(()-> new NullPointerException("유저가 존재하지 않네요."));

        if(!isRelationship(currentLoginUser, sender)) {
            currentLoginUser.setUserRelation(sender);
            sender.setUserRelation(currentLoginUser);
            userRepository.save(currentLoginUser);
            userRepository.save(sender);
            notificationRepository.deleteBy_id(notificationId);
            System.out.println("친구 등록이 완료되었습니다.");
        } else {
            System.out.println("이미 등록된 친구입니다.");
        }

    }

    public void relationshipRemove(Authentication authentication, Long friendId) {
        User currentLoginUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저에요."));

        System.out.println("currentLoginUser.getId() = " + currentLoginUser.getId());

        User beDeletedUser = userRepository.findById(friendId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저에요."));

        System.out.println("beDeletedUser.getId() = " + beDeletedUser.getId());


        //자기 자신과의 친구 막아야함! -> test때문에 아직 처리 x
        if(isRelationship(currentLoginUser, beDeletedUser)){
            currentLoginUser.deleteUserRelation(beDeletedUser);
            beDeletedUser.deleteUserRelation(currentLoginUser);
            userRepository.save(currentLoginUser);
            userRepository.save(beDeletedUser);
        } else {
            System.out.println("이미 친구 관계가 아니에요.");
        }

    }
    public void relationshipReject(Authentication authentication, String notificationId) {
        User currentLoginUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NullPointerException("존재하지 않는 유저에요."));

        Notification notification = notificationRepository.findBy_id(notificationId).
                orElseThrow(()-> new NullPointerException("해당 알림이 존재하지 않아요."));

        if(currentLoginUser.getId() != notification.getReceiverId()){
            System.out.println("접근할 수 없는 알림이에요. (알림 수신자와 현재 로그인 유저가 달라요.)");
            return;
        }

        notificationRepository.deleteBy_id(notificationId);
    }


    public boolean isRelationship(User user1, User user2) {
        if(user1.getUserRelation().contains(user2) && user2.getUserRelation().contains(user1))
            return true;
        else
            return false;
    }

}
