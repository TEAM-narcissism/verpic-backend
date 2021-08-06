package teamverpic.verpicbackend.domain.preview.dto.expression;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.preview.domain.Expression;

@Getter
@NoArgsConstructor
public class ExpressionSaveRequestDto {

    private String word;
    private String meaning;
    private String example;
    private String pronounce;

    @Builder
    public ExpressionSaveRequestDto(String word, String meaning, String example, String pronounce) {
        this.word = word;
        this.meaning = meaning;
        this.example = example;
        this.pronounce = pronounce;
    }

    public Expression toEntity() {
        Expression expression = Expression.builder()
                .word(word)
                .meaning(meaning)
                .example(example)
                .pronounce(pronounce)
                .build();
        return expression;
    }
}
