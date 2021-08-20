package teamverpic.verpicbackend.domain.matching.dto;

import lombok.Data;

@Data
public class ParticipantCheckDto {

    boolean result;

    public ParticipantCheckDto(boolean result) {
        this.result = result;
    }
}
