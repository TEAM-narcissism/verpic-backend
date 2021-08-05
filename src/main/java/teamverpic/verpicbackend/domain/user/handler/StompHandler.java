package teamverpic.verpicbackend.domain.user.handler;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import teamverpic.verpicbackend.config.security.JwtTokenProvider;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
//@Component
public class StompHandler implements ChannelInterceptor  {
    private final JwtTokenProvider jwtTokenProvider;

    @Getter
    List<String> sessionList = new ArrayList<String>();

    @Getter
    List<String> doingSocketUserList = new ArrayList<String>();


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        Principal currentUser = accessor.getUser();


        if(accessor.getCommand() == StompCommand.CONNECT) {
            /*
            if(!jwtTokenProvider.validateToken(token)){
                System.out.println("토큰 이상");
            }
            else {
                System.out.println("토큰 정상");
            }
            //throw new AccessDeniedException("토큰 이상, 임시 메시지 입니다.");
            */
            sessionList.add(accessor.getSessionId());
            doingSocketUserList.add(currentUser.getName());

            System.out.println("currentUser = " + currentUser.getName());
            System.out.println("accessor.getSessionId() = " + accessor.getSessionId());
            System.out.println();

            System.out.println("######################## CONNECT -- 현재 접속 유저 ########################");
            doingSocketUserList.forEach(user -> System.out.println("user = " + user));
            System.out.println("#########################################################################");
            System.out.println();
        }


        else if(accessor.getCommand() == StompCommand.DISCONNECT) {

            doingSocketUserList.remove(currentUser.getName());
            sessionList.remove(accessor.getSessionId());

            System.out.println("######################## DISCONNECT -- 현재 접속 유저 ########################");
            doingSocketUserList.forEach(user -> System.out.println("user = " + user));
            System.out.println("############################################################################");
            System.out.println();
        }

        return message;
    }

}
