package teamverpic.verpicbackend.domain.preview.dto.useranswer;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.domain.UserAnswer;

@Getter
public class UserAnswerResponseDto {
    private Long user_id;
    private String answer;

    public UserAnswerResponseDto(UserAnswer entity) {
        this.user_id = entity.getUser_id();
        this.answer = entity.getAnswer();
    }
}
