package teamverpic.verpicbackend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;


@Document(collection="notifications")
public class Notification {

    public Notification() {
    }

    @Builder
    public Notification(String message, Long receiver, Long sender) {
        this.message = message;
        this.receiverId = receiver;
        this.senderId = sender;
    }
    @Id
    private String _id;


    @Getter @Setter
    private String message;

    @Getter @Setter
    private Long receiverId;

    @Getter @Setter
    private Long senderId;


}
