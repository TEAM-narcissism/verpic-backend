package teamverpic.verpicbackend.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import teamverpic.verpicbackend.domain.ChatRoom;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.repository.ChatRoomRepository;
import teamverpic.verpicbackend.repository.UserRepository;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatRoomSubscriptionInterceptor implements ChannelInterceptor {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final Map<Long, Set<String>> roomId2SessionIDs = new HashMap<>();
    private final Map<String, Long> sessionId2RoomId = new HashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        accessor.setLeaveMutable(false);
        switch (accessor.getCommand()) {
            case SUBSCRIBE:
                System.out.println("Subscribe");
                log.info("subscribe destination : {}", accessor.getDestination());
                if(accessor.getDestination().startsWith("/sub/chat/")) {
                    if (!validateSubscription(accessor.getDestination(), accessor.getUser().getName())) {
                        throw new IllegalArgumentException("해당 유저는 이 채팅방의 유저가 아닙니다");
                    }
                }
                else {
                    break;
                }

                Long roomId = getRoomIdFromDestination(accessor.getDestination());
                sessionId2RoomId.put(sessionId, roomId);
                if(roomId2SessionIDs.containsKey(roomId)) {
                    roomId2SessionIDs.get(roomId).add(sessionId);
                }
                else {
                    roomId2SessionIDs.put(roomId, new HashSet<>(Arrays.asList(sessionId)));
                }
                System.out.println("CONNECTED");
                sessionId2RoomId.forEach((key, val) -> log.info("key = {}, val = {}", key, val));
                break;
            case DISCONNECT:
                if(sessionId2RoomId.containsKey(sessionId)) {
                    roomId2SessionIDs.remove(sessionId2RoomId.get(sessionId));
                    sessionId2RoomId.remove(sessionId);
                    System.out.println("DISCONNECTED");
                    sessionId2RoomId.forEach((key, val) -> log.info("key = {}, val = {}", key, val));
                }
                break;
            default:
                break;
        }

        return message;
    }

    private boolean validateSubscription(String destination, String username) {
        if (destination.startsWith("/sub/chat/")) {
            Long roomId = getRoomIdFromDestination(destination);
            ChatRoom room = chatRoomRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new NoSuchElementException("해당 채팅방이 존재하지 않습니다"));
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다"));
            if (!room.isParticipant(user.getId())) {
                return false;
            }
        }
        return true;
    }

    private Long getRoomIdFromDestination(String destination) {
        try {
            return Long.valueOf(destination.split("/")[3]);
        }
        catch (Exception e) {
            log.info("잡았따!");
            return null;
        }
    }
}
