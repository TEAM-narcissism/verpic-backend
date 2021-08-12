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
    private String message;
    private Date timeStamp;

    public VideoChatMessage toEntity() {

        return VideoChatMessage.builder()
                .matchId(this.matchId)
                .senderName(this.senderName)
                .message(this.message)
                .timeStamp(this.timeStamp)
                .build();
    }
}
