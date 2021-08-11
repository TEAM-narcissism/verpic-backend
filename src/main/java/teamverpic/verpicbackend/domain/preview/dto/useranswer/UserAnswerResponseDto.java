package teamverpic.verpicbackend.domain.preview.dto.useranswer;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.domain.UserAnswer;

@Getter
public class UserAnswerResponseDto {
    private String answer;

    public UserAnswerResponseDto(UserAnswer entity) {
        this.answer = entity.getAnswer();
    }
}
