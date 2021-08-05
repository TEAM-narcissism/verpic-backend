package teamverpic.verpicbackend.dto.preview.useranswer;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.UserAnswer;

@Getter
public class UserAnswerResponseDto {
    private Long id;
    private Long user_id;
    private String answer;

    public UserAnswerResponseDto(UserAnswer entity) {
        this.id = entity.getId();
        this.user_id = entity.getUser_id();
        this.answer = entity.getAnswer();
    }
}
