package teamverpic.verpicbackend.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class MostUsedWordDto {
    private String word;
    private Integer count;
}
