package teamverpic.verpicbackend.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import teamverpic.verpicbackend.domain.ChatRoom;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.repository.ChatRoomRepository;
import teamverpic.verpicbackend.repository.UserRepository;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class ChatRoomSubscriptionInterceptor implements ChannelInterceptor {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            System.out.println("Subscribe");
            if (!validateSubscription(accessor.getDestination(), accessor.getUser().getName())) {
                throw new IllegalArgumentException("해당 유저는 이 채팅방의 유저가 아닙니다");
            }
        }
//        else if(StompCommand..equals(accessor.getCommand())) {
//            System.out.println("dest:" + accessor.getDestination());
//            if(accessor.getUser().getName().contains("/sub/chat/")) {
//                System.out.println("Unsubscribe");
//            }
//        }
        return message;
    }

    private boolean validateSubscription(String destination, String username) {
        if (destination.contains("/sub/chat/")) {
            Long roomId = Long.valueOf(destination.split("/")[3]);
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
}
