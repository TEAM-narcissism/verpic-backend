package teamverpic.verpicbackend.domain.studyroom.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.*;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.matching.service.MatchService;

import teamverpic.verpicbackend.domain.studyroom.domain.WebSocketMessage;
import teamverpic.verpicbackend.domain.studyroom.dto.WebSocketMessageDto;


import java.util.*;

@Controller
@RequiredArgsConstructor
public class StompCommandListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MatchService matchService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Getter
    List<String> socketParticipateUserList = new ArrayList<String>();
    List<String> socketSessionList = new ArrayList<String>();

    private Map<String, String> sessionId2UserEmailMap = new HashMap<>();
    private Map<String, Match> sessionId2Match = new HashMap<>();
    private Map<String, Match> uuid2Match = new HashMap<>();
    private Map<String, String> sessionId2uuid = new HashMap<>();


    private static final String MSG_TYPE_OFFER = "offer";
    private static final String MSG_TYPE_ANSWER = "answer";
    private static final String MSG_TYPE_ICE = "ice";
    private static final String MSG_TYPE_JOIN = "join";
    private static final String MSG_TYPE_LEAVE = "leave";


    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        try {
            socketParticipateUserList.add(event.getUser().getName());
        }catch(Exception e) {
           
        }
        socketSessionList.add(event.getMessage().getHeaders().get("simpSessionId").toString());

    }


    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event){
        try {
            socketParticipateUserList.remove(event.getUser().getName());
        }catch(Exception e) {

        }
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        socketSessionList.remove(sessionId);

        if(sessionId2Match.containsKey(sessionId)) {
            Match match = sessionId2Match.get(sessionId);
            removeUserFromRoom(sessionId);

            int count = countUserOfMatch(match);
            logger.info("Disconnect : 현재 이 방의 클라이언트 수: {}", count);
            if (count <= 1) {
                for (Map.Entry<String, Match> cli : uuid2Match.entrySet()) {
                    sendMessage(cli.getKey(), new WebSocketMessage(
                            "Server",
                            MSG_TYPE_JOIN,
                            "false",
                            null,
                            null));
                }
            }
        }

    }

    @MessageMapping("/video-signal")
    public void handleTextMessage(WebSocketMessageDto textMessage, SimpMessageHeaderAccessor headerAccessor) {
        WebSocketMessage message = textMessage.toEntity();
        Authentication authentication = getAuthentication(headerAccessor);
        logger.info(authentication.getName());
        String sessionId = headerAccessor.getSessionId();

        logger.debug("[ws] Message of {} type from {} received", message.getType(), message.getFrom());
        String userName = message.getFrom(); // origin of the message,, uuid
        String data = message.getData(); // payload

        Match match;
        switch (message.getType()) {
            case MSG_TYPE_OFFER:
            case MSG_TYPE_ANSWER:
            case MSG_TYPE_ICE:
                Object candidate = message.getCandidate();
                Object sdp = message.getSdp();
                match = sessionId2Match.get(sessionId);

                if(match != null) {
                    List<String> uuidOfMatch = uuidListOfMatch(match);

                    uuidOfMatch.forEach(
                            (uuid)-> {
                                if(!uuid.equals(userName)) {
                                sendMessage(
                                        uuid, new WebSocketMessage(
                                                userName,
                                                message.getType(),
                                                data,
                                                candidate,
                                                sdp)
                                );
                            }});

                }

                break;

            case MSG_TYPE_JOIN:
                logger.info("[ws] {} has joined Room: #{}", userName, message.getData());

                match = matchService.findByMatchId(data);
                sessionId2UserEmailMap.put(sessionId, authentication.getName());
                sessionId2Match.put(sessionId, match);
                uuid2Match.put(userName, match);
                sessionId2uuid.put(sessionId, userName);

                int userCount = countUserOfMatch(match);
                logger.info("Join: 현재 {} 방의 클라이언트 수: {}", match.getId(), userCount);

                String offerData = "true";

                if(userCount <=1) {
                    offerData= "false";
                }

                sendMessage(userName, new WebSocketMessage(
                            "Server",
                            MSG_TYPE_JOIN,
                            offerData,
                            null,
                            null));


                break;

            case MSG_TYPE_LEAVE:
                logger.info("[ws] {} is going to leave Room: #{}", userName, message.getData());
                break;

            default:
                logger.debug("[ws] Type of the received message {} is undefined!", message.getType());
        }

    }
    private void sendMessage(String uuid, WebSocketMessage message) {
        messagingTemplate.convertAndSend("/sub/video-signal/" + uuid, message);
    }
    private Authentication getAuthentication(SimpMessageHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        return jwtTokenProvider.getAuthentication(token);
    }

    int countUserOfMatch(Match match) {
        int count = 0;

        for(Map.Entry<String, Match> uuidMatch : uuid2Match.entrySet())  {
            if(uuidMatch.getValue().getId() == match.getId()) {
                count++;
            }
        }
        return count;
    }

    List<String> uuidListOfMatch(Match match) {
        List<String> uuidList = new ArrayList<>();

        for(Map.Entry<String, Match> uuidMatch : uuid2Match.entrySet())  {
            if(uuidMatch.getValue().getId() == match.getId()) {
                uuidList.add(uuidMatch.getKey());
            }
        }

        return uuidList;
    }

    public void removeUserFromRoom(String sessionId) {
        sessionId2Match.remove(sessionId);
        sessionId2UserEmailMap.remove(sessionId);
        String userName = sessionId2uuid.get(sessionId);
        sessionId2uuid.remove(sessionId);
        uuid2Match.remove(userName);

    }

}
