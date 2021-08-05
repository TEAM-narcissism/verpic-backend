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
    private final RelationshipService relationshipService;

    public ChatRoom chatRoomJoin(String userName, String receiverName) {
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다(로그인된 사용자)"));
        User receiver = userRepository.findByEmail(receiverName).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다(receiver)"));
        // 두 유저가 서로 친구관계인지 확인
//        if (!relationshipService.isRelationship(user, receiver)) {
//            throw new NullPointerException("두 유저가 친구이지 않습니다");
//        }

        Long userId1 = Math.min(user.getId(), receiver.getId());
        Long userId2 = Math.max(user.getId(), receiver.getId());

        ChatRoom room;
        try {
            // 이미 방이 존재하는 경우
            room = chatRoomRepository.findByParticipantsId1AndParticipantsId2(userId1, userId2).get();
        } catch (NoSuchElementException e) {
            // 방이 존재하지 않는 경우
            room = chatRoomRepository.save(ChatRoom.builder()
                    .participantsId1(userId1)
                    .participantsId2(userId2)
                    .build());
        }

        return room;
    }
}
