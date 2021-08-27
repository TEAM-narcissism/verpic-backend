package teamverpic.verpicbackend.domain.chat.dto;

import lombok.Data;

@Data
public class ChatEnterDto {
    private Long matchUserId;

    public ChatEnterDto(Long matchUserId) {
        this.matchUserId = matchUserId;
    }
}
