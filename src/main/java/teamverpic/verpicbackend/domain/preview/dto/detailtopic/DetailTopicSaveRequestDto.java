package teamverpic.verpicbackend.domain.preview.dto.detailtopic;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;
import teamverpic.verpicbackend.domain.preview.domain.UserAnswer;

import java.util.List;

@Getter
@NoArgsConstructor
public class DetailTopicSaveRequestDto {

    private String context;

    @Builder
    public DetailTopicSaveRequestDto(String context) {
        this.context = context;
    }

    public DetailTopic toEntity() {
        DetailTopic detailTopic = DetailTopic.builder()
                .context(context)
                .build();
        return detailTopic;
    }
}
