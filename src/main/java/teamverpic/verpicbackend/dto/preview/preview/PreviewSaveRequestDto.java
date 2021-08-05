package teamverpic.verpicbackend.dto.preview.preview;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.preview.DetailTopic;
import teamverpic.verpicbackend.domain.preview.Expression;
import teamverpic.verpicbackend.domain.preview.Preview;

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
