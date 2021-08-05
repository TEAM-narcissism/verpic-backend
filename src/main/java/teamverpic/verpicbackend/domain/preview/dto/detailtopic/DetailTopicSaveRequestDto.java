package teamverpic.verpicbackend.domain.preview.dto.detailtopic;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;
import teamverpic.verpicbackend.domain.preview.domain.UserAnswer;

import java.util.List;

@Getter
@NoArgsConstructor
public class DetailTopicSaveRequestDto {

    private String context;
    private List<UserAnswer> userAnswerList;

    @Builder
    public DetailTopicSaveRequestDto(String context, List<UserAnswer> userAnswerList) {
        this.context = context;
        this.userAnswerList = userAnswerList;
    }

    public DetailTopic toEntity() {
        DetailTopic detailTopic = DetailTopic.builder()
                .context(context)
                .userAnswerList(userAnswerList)
                .build();
        return detailTopic;
    }
}
