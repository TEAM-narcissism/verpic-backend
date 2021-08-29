package teamverpic.verpicbackend.domain.preview.dto.detailtopic;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;

@Getter
public class DetailTopicResponseDto {
    private String context;
    private Long id;

    public DetailTopicResponseDto(DetailTopic entity) {
        this.id = entity.getId();
        this.context = entity.getContext();
    }
}
