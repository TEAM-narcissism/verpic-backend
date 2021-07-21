package teamverpic.verpicbackend.handler;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.*;
import teamverpic.verpicbackend.domain.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class StompCommandListener {
    private SimpMessagingTemplate template;

    @Getter
    List<String> socketParticipateUserList = new ArrayList<String>();
    List<String> socketSessionList = new ArrayList<String>();

    @Autowired
    public StompCommandListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {

        socketParticipateUserList.add(event.getUser().getName());
        socketSessionList.add(event.getMessage().getHeaders().get("simpSessionId").toString());

        MessageHeaders headers = event.getMessage().getHeaders();

        headers.forEach((key, data) ->
                System.out.println("(key, data): " + key + "," + data)
        );

        System.out.println();
        System.out.println("######################## CONNECT -- 현재 접속 유저 ########################");
        socketParticipateUserList.forEach(user -> System.out.println("user = " + user));
        socketSessionList.forEach(session -> System.out.println("session = " + session));
        System.out.println("#########################################################################");
        System.out.println();
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event){
        socketParticipateUserList.remove(event.getUser().getName());
        socketSessionList.remove(event.getMessage().getHeaders().get("simpSessionId").toString());

        System.out.println();
        System.out.println("######################## DISCONNECT -- 현재 접속 유저 ########################");
        socketParticipateUserList.forEach(user -> System.out.println("user = " + user));
        System.out.println("############################################################################");
        System.out.println();
    }
    

}
