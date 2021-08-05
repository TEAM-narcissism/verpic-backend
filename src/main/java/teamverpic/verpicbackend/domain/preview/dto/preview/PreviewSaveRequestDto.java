package teamverpic.verpicbackend.domain.preview.dto.preview;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;
import teamverpic.verpicbackend.domain.preview.domain.Expression;
import teamverpic.verpicbackend.domain.preview.domain.Preview;

import java.util.List;

@Getter
@NoArgsConstructor
public class PreviewSaveRequestDto {

    private String subject;
    private String context;
    private List<DetailTopic> detailTopicList;
    private List<Expression> expressionList;

    @Builder
    public PreviewSaveRequestDto(String subject, String context, List<DetailTopic> detailTopicList, List<Expression> expressionList) {
        this.subject = subject;
        this.context = context;
        this.detailTopicList = detailTopicList;
        this.expressionList = expressionList;
    }

    public Preview toEntity() {
        Preview preview = Preview.builder()
                .subject(subject)
                .context(context)
                .detailTopicList(detailTopicList)
                .expressionList(expressionList)
                .build();
        return preview;
    }
}
