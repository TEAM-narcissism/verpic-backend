package teamverpic.verpicbackend.domain.preview.dto.preview;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.domain.Preview;

@Getter
public class PreviewResponseDto {
    private String context;

    public PreviewResponseDto(Preview entity) {
        this.context = entity.getContext();
    }
}
