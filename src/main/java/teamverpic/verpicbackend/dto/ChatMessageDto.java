package teamverpic.verpicbackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class ChatMessageDto {
    private Long roomId;
    private String senderName;
    private String receiverName;
    private String message;
    private Date timeStamp;
}
