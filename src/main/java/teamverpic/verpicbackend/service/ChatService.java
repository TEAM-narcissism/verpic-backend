package teamverpic.verpicbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.ChatMessage;
import teamverpic.verpicbackend.dto.ChatMessageDto;
import teamverpic.verpicbackend.repository.ChatRepository;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate template;
    private final ChatRepository chatRepository;

    public void chatEnter(ChatMessageDto message, String userName) {
        message.setSenderName(userName);
        message.setTimeStamp(new Date());
        message.setMessage("채팅방 입장 성공");

        template.convertAndSend("/sub/chat/" + message.getRoomId(), message);
    }

    public ChatMessageDto chatMessageCreate(ChatMessageDto message, String userName) {
        message.setSenderName(userName);
        message.setTimeStamp(new Date());
        ChatMessage chatMessage = ChatMessage.builder()
                .messageDto(message)
                .build();
        chatRepository.save(chatMessage);

        return message;
    }

    public void chatSend(ChatMessageDto message, String userName) {
        message = chatMessageCreate(message, userName);
        template.convertAndSend("/sub/chat/" + message.getRoomId(), message);
    }
}
