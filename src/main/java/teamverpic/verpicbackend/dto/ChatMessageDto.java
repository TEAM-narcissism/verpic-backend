package teamverpic.verpicbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import teamverpic.verpicbackend.domain.ChatMessage;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ChatMessageDto {
    private Long roomId;
    private String senderName;
    private String receiverName;
    private String message;
    private Date timeStamp;

    public static ChatMessageDto from(ChatMessage entity) {
        return new ChatMessageDto(
                entity.getRoomId(),
                entity.getSenderName(),
                entity.getReceiverName(),
                entity.getMessage(),
                entity.getTimeStamp());
    }
}
