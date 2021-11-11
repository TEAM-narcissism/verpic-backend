package teamverpic.verpicbackend.domain.preview.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "preview")
public class Preview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String context;

    @OneToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "preview")
    private List<DetailTopic> detailTopicList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "preview")
    private List<Expression> expressionList;

    public void addDetailTopic(DetailTopic detailTopic) {
        detailTopic.setPreview(this);
        this.detailTopicList.add(detailTopic);
    }

    public void addExpression(Expression expression) {
        expression.setPreview(this);
        this.expressionList.add(expression);
    }

    @Builder
    private Preview(String context, Topic topic, List<DetailTopic> detailTopicList, List<Expression> expressionList) {
        this.context = context;
        this.topic = topic;
        this.detailTopicList = detailTopicList;
        this.expressionList = expressionList;
    }
}
