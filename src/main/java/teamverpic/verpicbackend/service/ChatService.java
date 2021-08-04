package teamverpic.verpicbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.ChatMessage;
import teamverpic.verpicbackend.domain.ChatRoom;
import teamverpic.verpicbackend.dto.ChatMessageDto;
import teamverpic.verpicbackend.handler.ChatRoomSubscriptionInterceptor;
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
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final String dest = "/sub/chat/";
    @Resource
    private final Map<Long, Set<String>> roomId2SessionIDs;
    @Resource
    private final Map<String, Long> sessionId2RoomId;


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
        // 상대방이 온라인인지 확인 후 그에 대한 처리 필요
        roomId2SessionIDs.get(message.getRoomId()).forEach(
                user -> {if (user != sessionId) {
                    read.set(true);
                }
        });
        message = chatMessageCreate(message, userName, read.get());
        template.convertAndSend(dest + message.getRoomId(), message);
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

    /**
     * @param username
     * @param roomId
     * @return 이메일로 해당 방에 참여한 유저 여부 반환
     */

    public boolean isParticipant(String username, Long roomId) {
        Long userId = userRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException(username + "유저가 없습니다")).getId();

        ChatRoom room = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NoSuchElementException("해당 방이 존재하지 않습니다"));

        return (room.getParticipantsId1() == userId || room.getParticipantsId2() == userId);
    }
}
