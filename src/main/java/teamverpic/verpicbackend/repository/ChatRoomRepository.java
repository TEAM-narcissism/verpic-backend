package teamverpic.verpicbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByParticipantsId1AndParticipantsId2(Long participantsId1, Long participantsId2);
    Optional<ChatRoom> findByRoomId(Long roomId);
}
