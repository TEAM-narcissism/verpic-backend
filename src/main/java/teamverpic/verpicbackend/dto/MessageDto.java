package teamverpic.verpicbackend.dto;


import lombok.Getter;
import lombok.Setter;
import teamverpic.verpicbackend.domain.User;

public class MessageDto {
    public MessageDto(String message, User receiver) {
        this.message = message;
        this.receiver = receiver;
    }

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private User receiver;

    public MessageDto() {
    }

    public Long getReceiverId() {
       return receiver.getId();
    }


}
