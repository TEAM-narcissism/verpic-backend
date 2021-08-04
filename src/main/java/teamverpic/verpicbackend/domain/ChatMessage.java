package teamverpic.verpicbackend.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import teamverpic.verpicbackend.dto.ChatMessageDto;

import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document(collection = "chat_messages")
public class ChatMessage {

    @Builder
    public ChatMessage(ChatMessageDto messageDto, boolean read) {
        this.roomId = messageDto.getRoomId();
        this.senderName = messageDto.getSenderName();
        this.receiverName = messageDto.getReceiverName();
        this.message = messageDto.getMessage();
        this.timeStamp = messageDto.getTimeStamp();
        this.read = read;
    }

    @Id
    private String id;
    private Long roomId;
    private String senderName;
    private String receiverName;
    private String message;
    private Date timeStamp;
    @Setter
    private Boolean read;
}
