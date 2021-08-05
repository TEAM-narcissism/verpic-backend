package teamverpic.verpicbackend.dto.preview.preview;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.DetailTopic;
import teamverpic.verpicbackend.domain.preview.Expression;
import teamverpic.verpicbackend.domain.preview.Preview;

import java.util.List;

@Getter
public class PreviewResponseDto {
    private Long id;
    private String subject;
    private String context;
//    private List<DetailTopic> detailTopicList;
//    private List<Expression> expressionList;

    public PreviewResponseDto(Preview entity) {
        this.id = entity.getId();
        this.subject = entity.getSubject();
        this.context = entity.getContext();
//        this.detailTopicList = entity.getDetailTopicList();
//        this.expressionList = entity.getExpressionList();
    }
}
