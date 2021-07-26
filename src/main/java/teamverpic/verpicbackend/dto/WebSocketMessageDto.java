package teamverpic.verpicbackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import teamverpic.verpicbackend.domain.WebSocketMessage;

@Getter
@Setter
@Data
public class WebSocketMessageDto {
    private String from;
    private String type;
    private String data;
    private Object candidate;
    private Object sdp;

    public WebSocketMessage toEntity(){
        WebSocketMessage build = WebSocketMessage.builder()
                .from(from)
                .type(type)
                .data(data)
                .candidate(candidate)
                .sdp(sdp)
                .build();

        return build;
    }
}
