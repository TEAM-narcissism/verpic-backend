package teamverpic.verpicbackend.domain.preview.dto.expression;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.domain.Expression;

@Getter
public class ExpressionResponseDto {
    private String word;
    private String meaning;
    private String example;
    private String audios;
    private String contentType;
    private byte[] pronounce;

    public ExpressionResponseDto(Expression entity) {
        this.word = entity.getWord();
        this.meaning = entity.getMeaning();
        this.example = entity.getExample();
        this.audios=entity.getAudios();
        this.contentType=entity.getContentType();
        this.pronounce = entity.getPronounce();
    }
}
