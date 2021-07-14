package teamverpic.verpicbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.dto.MessageDto;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final SimpMessageSendingOperations messagingTemplate;

    public void alarmByMessage(MessageDto messageDto) {
        messagingTemplate.convertAndSend("/sub/"+ messageDto.getReceiverId(), messageDto.getMessage());
    }
}
