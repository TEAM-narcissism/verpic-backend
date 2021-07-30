package teamverpic.verpicbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.ChatMessage;
import teamverpic.verpicbackend.domain.ChatRoom;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.dto.ChatMessageDto;
import teamverpic.verpicbackend.repository.ChatRepository;
import teamverpic.verpicbackend.repository.ChatRoomRepository;
import teamverpic.verpicbackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate template;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SimpUserRegistry userRegistry;
    private final String dest = "/sub/chat/";

    public void chatEnter(ChatMessageDto message, String userName) {
        message.setSenderName(userName);
        message.setTimeStamp(new Date());
        message.setMessage("채팅방 입장 성공");
        template.convertAndSend("/sub/chat/" + message.getRoomId(), message);
    }

    public void chatSend(ChatMessageDto message, String userName) {
        message = chatMessageCreate(message, userName);
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
