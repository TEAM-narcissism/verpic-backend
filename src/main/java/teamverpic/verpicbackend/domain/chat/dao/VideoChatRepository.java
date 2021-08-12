package teamverpic.verpicbackend.domain.chat.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import teamverpic.verpicbackend.domain.chat.domain.VideoChatMessage;

public interface VideoChatRepository extends MongoRepository<VideoChatMessage, String> {
}
