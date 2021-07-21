package teamverpic.verpicbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.ChatRoom;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.repository.ChatRoomRepository;
import teamverpic.verpicbackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom chatRoomJoin(String userName, String receiverName) {
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다(로그인된 사용자)"));
        User receiver = userRepository.findByEmail(receiverName).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다(receiver)"));
        // 두 유저가 서로 친구관계인지 확인하는 기능 필요!
        Long participantsId1 = Math.min(user.getId(), receiver.getId());
        Long participantsId2 = Math.max(user.getId(), receiver.getId());

        ChatRoom room;
        try {
            // 이미 방이 존재하는 경우
            room = chatRoomRepository.findByParticipantsId1AndParticipantsId2(participantsId1, participantsId2).get();
        } catch (NoSuchElementException e) {
            // 방이 존재하지 않는 경우
            room = chatRoomRepository.save(ChatRoom.builder()
                    .participantsId1(participantsId1)
                    .participantsId2(participantsId2)
                    .build());
        }

        return room;
    }
}
