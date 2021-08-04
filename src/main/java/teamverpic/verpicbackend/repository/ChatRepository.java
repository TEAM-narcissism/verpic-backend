package teamverpic.verpicbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import teamverpic.verpicbackend.domain.ChatMessage;
import teamverpic.verpicbackend.dto.ChatMessageDto;

import java.util.Date;
import java.util.List;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {
    public List<ChatMessageDto> findAllByRoomId(Long senderID, Long receiverID);

    public List<ChatMessage> findAllByRoomIdAndReceiverNameAndReadFalse(Long roomId, String receiverName);
    public List<ChatMessageDto> findTop10ByRoomIdAndTimeStampBeforeOrderByTimeStampDesc(Long roomId, Date timeStamp);
}
