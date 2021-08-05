package teamverpic.verpicbackend.dto.preview.expression;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.Expression;

@Getter
public class ExpressionResponseDto {
    private String word;
    private String meaning;
    private String example;
    private String pronounce;

    public ExpressionResponseDto(Expression entity) {
        this.word = entity.getWord();
        this.meaning = entity.getMeaning();
        this.example = entity.getExample();
        this.pronounce = entity.getPronounce();
    }
}
