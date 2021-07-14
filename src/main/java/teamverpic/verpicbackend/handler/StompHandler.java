package teamverpic.verpicbackend.handler;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.config.websocket.WebSocketConfig;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor  {
    private final JwtTokenProvider jwtTokenProvider;

    @Getter
    List<String> sessionList = new ArrayList<String>();

    @Getter
    List<String> doingSocketUserList = new ArrayList<String>();
    // 유저 리스트를 따로 두지말고, <세션, 유저> 형태의 hashmap 형태 필요
    // disconnect시 유저 아이디를 헤더로 전송할 수 없음
    // 즉, 지금은 유저가 소켓 연결 해제 시 리스트에 반영되지 않는 상태임

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            // 토큰 검
            if(!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token")));
                //throw new AccessDeniedException("토큰 이상, 임시 메시지 입니다.");

            sessionList.add(accessor.getSessionId());

            String userId = accessor.getFirstNativeHeader("senderId");
            System.out.println("requestUserId = " + userId);

            doingSocketUserList.add(userId);
            System.out.println("accessor.getSessionId() = " + accessor.getSessionId());
        }

        else if(accessor.getCommand() == StompCommand.DISCONNECT) {
            String userId = accessor.getFirstNativeHeader("senderId");
            System.out.println("requestUserId = " + userId);

            doingSocketUserList.remove(userId);
            sessionList.remove(accessor.getSessionId());
            System.out.println("accessor.getSessionId() = " + accessor.getSessionId());

        }

        return message;
    }

}
