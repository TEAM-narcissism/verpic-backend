package teamverpic.verpicbackend.domain.preview.dto.useranswer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.preview.domain.UserAnswer;

@Getter
@NoArgsConstructor
public class UserAnswerSaveRequestDto {
    private String answer;

    @Builder
    public UserAnswerSaveRequestDto(String answer) {
        this.answer = answer;
    }

    public UserAnswer toEntity() {
        return UserAnswer.builder()
                .answer(answer)
                .build();
    }
}
