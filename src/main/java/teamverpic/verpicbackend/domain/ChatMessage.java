package teamverpic.verpicbackend.domain;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import teamverpic.verpicbackend.dto.ChatMessageDto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Document(collation = "messages")
public class ChatMessage {

    @Builder
    public ChatMessage(ChatMessageDto messageDto) {
        this.roomId = messageDto.getRoomId();
        this.senderName = messageDto.getSenderName();
        this.receiverName = messageDto.getReceiverName();
        this.message = messageDto.getMessage();
        this.timeStamp = messageDto.getTimeStamp();
    }

    @Id
    private String id;
    private Long roomId;
    private String senderName;
    private String receiverName;
    private String message;
    private Date timeStamp;
}
