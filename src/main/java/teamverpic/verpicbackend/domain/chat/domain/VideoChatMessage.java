package teamverpic.verpicbackend.domain.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Document(collection = "videochat_messages")
public class VideoChatMessage {

    @Id
    private String id;
    private Long matchId;
    private Long matchUserId;
    private String message;
    private Date timeStamp;
}
