package teamverpic.verpicbackend.domain.preview.dto.useranswer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAnswerUpdateRequestDto {
    private String answer;

    @Builder
    public UserAnswerUpdateRequestDto(String answer) {
        this.answer = answer;
    }
}
