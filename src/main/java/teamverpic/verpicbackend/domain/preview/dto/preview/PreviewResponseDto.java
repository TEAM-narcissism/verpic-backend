package teamverpic.verpicbackend.domain.preview.dto.preview;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.domain.Preview;

@Getter
public class PreviewResponseDto {
    private Long id;
    private String context;

    public PreviewResponseDto(Preview entity) {
        this.id = entity.getId();
        this.context = entity.getContext();
    }
}
