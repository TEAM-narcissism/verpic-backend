package teamverpic.verpicbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.Notification;
import teamverpic.verpicbackend.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessageSendingOperations messagingTemplate;
    private final NotificationRepository notificationRepository;

    public Notification alarmCreate(Long sender, Long receiver, int alarmCategory) {

        String alarmMessage = "default message";

        // 나중에 알람 카테고리를 enum으로 refactoring하자.
        if(alarmCategory == 1)
            alarmMessage = "친구 요청이 왔습니다.";


       Notification notification =  Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .message(alarmMessage)
                .build();

      notificationRepository.save(notification);
      return notification;
    }


    public void alarmByMessage(Long sender, Long receiver, int alarmCategory) {
        Notification notification = alarmCreate(sender, receiver, alarmCategory);
        messagingTemplate.convertAndSend("/sub/"+ notification.getReceiverId(),notification.getMessage());
    }
}
