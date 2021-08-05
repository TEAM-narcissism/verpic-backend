package teamverpic.verpicbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.ChatMessage;
import teamverpic.verpicbackend.dto.ChatMessageDto;
import teamverpic.verpicbackend.repository.ChatRepository;
import teamverpic.verpicbackend.repository.ChatRoomRepository;
import teamverpic.verpicbackend.repository.UserRepository;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate template;
    private final ChatRepository chatRepository;
    private final String dest = "/sub/chat/";
    @Resource
    private final Map<Long, Set<String>> roomId2SessionIDs;

    public void chatEnter(ChatMessageDto message, String userName) {
        message.setSenderName("Verpic");
        message.setTimeStamp(new Date());
        message.setMessage(userName + "님이 입장하셨습니다.");
        List<ChatMessage> unreadMessages = chatRepository
                .findAllByRoomIdAndReceiverNameAndReadFalse(message.getRoomId(), userName);
        unreadMessages.forEach(m -> {
            m.setRead(true);
            chatRepository.save(m);
            template.convertAndSendToUser(userName, dest + message.getRoomId(), ChatMessageDto.from(m));
        });
        template.convertAndSend(dest + message.getRoomId(), message);
    }

    public void chatSend(ChatMessageDto message, String userName, String sessionId) {
        AtomicBoolean read = new AtomicBoolean(false);
        roomId2SessionIDs.get(message.getRoomId()).forEach(
                user -> {if (user != sessionId) {
                    read.set(true);
                }
        });
        message = chatMessageCreate(message, userName, read.get());
        template.convertAndSend(dest + message.getRoomId(), message);
    }

    public void chatLoad(Long roomId, Date timeStamp, String userName) {
        List<ChatMessageDto> messages = chatRepository.findTop10ByRoomIdAndTimeStampBeforeOrderByTimeStampDesc(roomId, timeStamp);
        messages.forEach(
                message -> {
                    template.convertAndSendToUser(userName, dest + roomId, message);
                });
    }

    public ChatMessageDto chatMessageCreate(ChatMessageDto message, String userName, boolean read) {
        message.setSenderName(userName);
        message.setTimeStamp(new Date());
        ChatMessage chatMessage = ChatMessage.builder()
                .messageDto(message)
                .read(read)
                .build();
        chatRepository.save(chatMessage);

        return message;
    }
}
