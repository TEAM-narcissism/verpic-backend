package teamverpic.verpicbackend.domain.chat.dto;

import lombok.*;
import teamverpic.verpicbackend.domain.chat.domain.VideoChatMessage;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoChatMessageDto {

    private Long matchId;
    private String senderName;
    private Long matchUserId;
    private String message;
    private Date timeStamp;

    public VideoChatMessage toEntity() {

        return VideoChatMessage.builder()
                .matchId(this.matchId)
                .matchUserId(this.matchUserId)
                .message(this.message)
                .timeStamp(this.timeStamp)
                .build();
    }
}
