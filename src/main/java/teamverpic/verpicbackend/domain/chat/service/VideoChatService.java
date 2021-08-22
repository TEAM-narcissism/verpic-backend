package teamverpic.verpicbackend.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.chat.dao.VideoChatRepository;
import teamverpic.verpicbackend.domain.chat.domain.VideoChatMessage;
import teamverpic.verpicbackend.domain.chat.dto.ChatEnterDto;
import teamverpic.verpicbackend.domain.chat.dto.ChatMessageDto;
import teamverpic.verpicbackend.domain.chat.dto.VideoChatMessageDto;
import teamverpic.verpicbackend.domain.matching.dao.MatchUserRepository;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;
import teamverpic.verpicbackend.domain.user.domain.User;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VideoChatService {

    private final VideoChatRepository videoChatRepository;
    private final MatchUserRepository matchUserRepository;
    private final SimpMessagingTemplate template;
    private final String dest = "/sub/videochat/";
    private final String adminName = "Verpic";

    public void enter(VideoChatMessageDto messageDto, User user, Principal principal) {
        String userName = user.getFirstName() + user.getLastName();
        MatchUser matchUser = matchUserRepository.findByUserIdAndMatchId(user.getId(), messageDto.getMatchId());

        messageDto.setSenderName(adminName);
        messageDto.setMessage(userName + "님이 입장하셨습니다.");
        messageDto.setTimeStamp(new Date());
        messageDto.setMatchUserId(0L);

        ChatEnterDto enterDto = new ChatEnterDto(matchUser.getId());

        template.convertAndSendToUser(principal.getName(), dest + messageDto.getMatchId(), enterDto);
        template.convertAndSend(dest + messageDto.getMatchId(), messageDto);

    }

    public void send(VideoChatMessageDto messageDto, User user) {
        String userName = user.getFirstName() + user.getLastName();

        messageDto.setSenderName(userName);
        messageDto.setTimeStamp(new Date());

        VideoChatMessage message = messageDto.toEntity();
        videoChatRepository.save(message);

        template.convertAndSend(dest + messageDto.getMatchId(), messageDto);
    }
}
