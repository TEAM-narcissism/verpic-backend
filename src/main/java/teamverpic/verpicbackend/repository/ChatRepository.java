package teamverpic.verpicbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import teamverpic.verpicbackend.domain.ChatMessage;
import teamverpic.verpicbackend.dto.ChatMessageDto;

import java.util.List;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {
    public List<ChatMessageDto>  findAllByRoomId(Long senderID, Long receiverID);
}
