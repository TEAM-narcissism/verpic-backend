package teamverpic.verpicbackend.dto.preview.useranswer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.preview.UserAnswer;

@Getter
@NoArgsConstructor
public class UserAnswerSaveRequestDto {
    private Long user_id;
    private String answer;

    @Builder
    public UserAnswerSaveRequestDto(Long user_id, String answer) {
        this.user_id = user_id;
        this.answer = answer;
    }

    public UserAnswer toEntity() {
        return UserAnswer.builder()
                .user_id(user_id)
                .answer(answer)
                .build();
    }
}
