package teamverpic.verpicbackend.dto.preview.detailtopic;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.DetailTopic;

@Getter
public class DetailTopicResponseDto {
    private String context;

    public DetailTopicResponseDto(DetailTopic entity) {
        this.context = entity.getContext();
    }
}
