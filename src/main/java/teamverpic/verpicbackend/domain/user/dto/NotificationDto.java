package teamverpic.verpicbackend.domain.user.dto;


import lombok.Getter;
import lombok.Setter;
import teamverpic.verpicbackend.domain.user.domain.User;

public class NotificationDto {
    public NotificationDto(String message, User receiver) {
        this.message = message;
        this.receiver = receiver;
    }

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private User receiver;

    public NotificationDto() {
    }

    public Long getReceiverId() {
       return receiver.getId();
    }


}
