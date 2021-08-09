package teamverpic.verpicbackend.domain.studyroom.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.*;
import teamverpic.verpicbackend.domain.studyroom.domain.VideoRoom;
import teamverpic.verpicbackend.domain.studyroom.domain.WebSocketMessage;
import teamverpic.verpicbackend.domain.studyroom.dto.WebSocketMessageDto;
import teamverpic.verpicbackend.domain.studyroom.service.VideoRoomService;

import java.io.IOException;
import java.util.*;

@Controller
public class StompCommandListener {
    private SimpMessagingTemplate messagingTemplate;
    private final VideoRoomService videoRoomService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    List<String> socketParticipateUserList = new ArrayList<String>();
    List<String> socketSessionList = new ArrayList<String>();
    private Map<String, VideoRoom> sessionIdToRoomMap = new HashMap<>();

    // message types, used in signalling:
    // text message
    private static final String MSG_TYPE_TEXT = "text";
    // SDP Offer message
    private static final String MSG_TYPE_OFFER = "offer";
    // SDP Answer message
    private static final String MSG_TYPE_ANSWER = "answer";
    // New ICE Candidate message
    private static final String MSG_TYPE_ICE = "ice";
    // join room data message
    private static final String MSG_TYPE_JOIN = "join";
    // leave room data message
    private static final String MSG_TYPE_LEAVE = "leave";

    @Autowired
    public StompCommandListener(SimpMessagingTemplate template, VideoRoomService videoRoomService) {
        this.messagingTemplate = template;
        this.videoRoomService = videoRoomService;
    }

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        try {
            socketParticipateUserList.add(event.getUser().getName());
        }catch(Exception e) {
           
        }
        socketSessionList.add(event.getMessage().getHeaders().get("simpSessionId").toString());

        MessageHeaders headers = event.getMessage().getHeaders();

        headers.forEach((key, data) ->
                System.out.println("(key, data): " + key + "," + data)
        );

        System.out.println();
        System.out.println("######################## CONNECT -- 현재 접속 유저 ########################");
        //socketParticipateUserList.forEach(user -> System.out.println("user = " + user));
        socketSessionList.forEach(session -> System.out.println("session = " + session));
        System.out.println("#########################################################################");
        System.out.println();
    }


    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event){
        try {
            socketParticipateUserList.remove(event.getUser().getName());
        }catch(Exception e) {

        }
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        socketSessionList.remove(sessionId);

        VideoRoom room = sessionIdToRoomMap.get(sessionId);
        Optional<String> client = videoRoomService.getClients(room).entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), sessionId))
                .map(Map.Entry::getKey)
                .findAny();
        client.ifPresent(c -> videoRoomService.removeClientByName(room, c));

        sessionIdToRoomMap.remove(sessionId);

        for(String i : sessionIdToRoomMap.keySet()){ //저장된 key값 확인
            logger.info("[Key]:" + i + " [Value]:" + sessionIdToRoomMap.get(i));
        }

        Map<String, String> clients = videoRoomService.getClients(room);
        logger.info("Disconnect : 현재 이 방의 클라이언트 수: {}", clients.size());
        if (clients.size() <= 1) {
            for(Map.Entry<String, String> cli : clients.entrySet())  {
                sendMessage(cli.getKey(), new WebSocketMessage(
                        "Server",
                        MSG_TYPE_JOIN,
                        "false",
                        null,
                        null));
            }
        }

        System.out.println();
        System.out.println("######################## DISCONNECT -- 현재 접속 유저 ########################");
        //socketParticipateUserList.forEach(user -> System.out.println("user = " + user));
        socketSessionList.forEach(session -> System.out.println("session = " + session));
        System.out.println("############################################################################");
        System.out.println();
    }

    @MessageMapping("/experiment")
    public void handleTextMessage(WebSocketMessageDto textMessage, SimpMessageHeaderAccessor headerAccessor) {
        WebSocketMessage message = textMessage.toEntity();

    try {
        String sessionId = headerAccessor.getSessionId();

        logger.debug("[ws] Message of {} type from {} received", message.getType(), message.getFrom());
        String userName = message.getFrom(); // origin of the message
        String data = message.getData(); // payload

        VideoRoom room;
        switch (message.getType()) {
            case MSG_TYPE_TEXT:
                logger.debug("[ws] Text message: {}", message.getData());
                break;

            case MSG_TYPE_OFFER:
            case MSG_TYPE_ANSWER:
            case MSG_TYPE_ICE:
                Object candidate = message.getCandidate();
                Object sdp = message.getSdp();

                VideoRoom rm = sessionIdToRoomMap.get(sessionId);
                if (rm != null) {
                    Map<String, String> clients = videoRoomService.getClients(rm);
                    for(Map.Entry<String, String> client : clients.entrySet())  {
                        if (!client.getKey().equals(userName)) {
                            sendMessage(client.getKey(), new WebSocketMessage(
                                            userName,
                                            message.getType(),
                                            data,
                                            candidate,
                                            sdp));
                        }
                    }
                }
                break;

            // identify user and their opponent
            case MSG_TYPE_JOIN:
                // message.data contains connected room id
                System.out.println("[ws]" + userName+ " has joined Room: #" +  message.getData());
                logger.info("[ws] {} has joined Room: #{}", userName, message.getData());
                room = videoRoomService.findRoomByStringId(data)
                        .orElseThrow(() -> new IOException("Invalid room number received!"));
                // add client to the Room clients list
                videoRoomService.addClient(room, userName, sessionId);
                sessionIdToRoomMap.put(sessionId, room);

                for(String i : sessionIdToRoomMap.keySet()){ //저장된 key값 확인
                    logger.info("[Key]:" + i + " [Value]:" + sessionIdToRoomMap.get(i));
                }

                Map<String, String> clients = videoRoomService.getClients(room);
                logger.info("Join: 현재 이 방의 클라이언트 수: {}", clients.size());
                    // uuid, sessionid로 이뤄짐

                    if (clients.size() <= 1) {
                        // select the same type to resend signal
                        sendMessage(userName, new WebSocketMessage(
                                "Server",
                                MSG_TYPE_JOIN,
                                "false",
                                null,
                                null));
                    } else {
                        sendMessage(userName, new WebSocketMessage(
                                "Server",
                                MSG_TYPE_JOIN,
                                "true",
                                null,
                                null));
                    }

                break;

            case MSG_TYPE_LEAVE:
                // message data contains connected room id
                logger.info("[ws] {} is going to leave Room: #{}", userName, message.getData());
                break;


            default:
                logger.debug("[ws] Type of the received message {} is undefined!", message.getType());
                // handle this if needed
        }

    } catch (IOException e) {
        logger.debug("An error occured: {}", e.getMessage());
    }

    }
    private void sendMessage(String uuid, WebSocketMessage message) {

        logger.info("sendmessage to {}", uuid);
        messagingTemplate.convertAndSend("/sub/" + uuid, message);

    }
}
