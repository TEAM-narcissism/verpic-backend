package teamverpic.verpicbackend.domain.preview.dto.preview;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;
import teamverpic.verpicbackend.domain.preview.domain.Expression;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.util.List;

@Getter
@NoArgsConstructor
public class PreviewSaveRequestDto {

    private String context;

    @Builder
    public PreviewSaveRequestDto(String context) {
        this.context = context;
    }

    public Preview toEntity() {
        Preview preview = Preview.builder()
                .context(context)
                .build();
        return preview;
    }
}
