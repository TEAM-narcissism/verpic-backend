package teamverpic.verpicbackend.domain.preview.dto.detailtopic;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;

@Getter
public class DetailTopicResponseDto {
    private String context;

    public DetailTopicResponseDto(DetailTopic entity) {
        this.context = entity.getContext();
    }
}
