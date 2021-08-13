package teamverpic.verpicbackend.domain.chat.dto;

import lombok.Data;

@Data
public class ChatEnterDto {
    private String name;

    public ChatEnterDto(String name) {
        this.name = name;
    }
}
