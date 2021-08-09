package teamverpic.verpicbackend.domain.preview.dto.preview;

import lombok.Getter;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

@Getter
public class PreviewResponseDto {
    private Long id;
    private String subject;
    private String context;
//    private Topic topic;
//    private List<DetailTopic> detailTopicList;
//    private List<Expression> expressionList;

    public PreviewResponseDto(Preview entity) {
        this.id = entity.getId();
        this.subject = entity.getSubject();
        this.context = entity.getContext();
//        this.topic = entity.getTopic();
//        this.detailTopicList = entity.getDetailTopicList();
//        this.expressionList = entity.getExpressionList();
    }
}
