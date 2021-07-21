package teamverpic.verpicbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamverpic.verpicbackend.domain.ChatRoom;
import teamverpic.verpicbackend.dto.ChatRoomDto;

import java.util.Optional;
import java.util.Set;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByParticipantsId1AndParticipantsId2(Long participantsId1, Long participantsId2);
    Optional<ChatRoom> findByRoomId(Long roomId);
}
