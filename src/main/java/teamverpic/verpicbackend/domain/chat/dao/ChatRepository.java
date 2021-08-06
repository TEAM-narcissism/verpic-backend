package teamverpic.verpicbackend.domain.chat.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import teamverpic.verpicbackend.domain.chat.domain.ChatMessage;
import teamverpic.verpicbackend.domain.chat.dto.ChatMessageDto;

import java.util.Date;
import java.util.List;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {
    public List<ChatMessageDto> findAllByRoomId(Long senderID, Long receiverID);

    public List<ChatMessage> findAllByRoomIdAndReceiverNameAndReadFalse(Long roomId, String receiverName);
    public List<ChatMessageDto> findTop10ByRoomIdAndTimeStampBeforeOrderByTimeStampDesc(Long roomId, Date timeStamp);
}
