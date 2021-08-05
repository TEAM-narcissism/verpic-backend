package teamverpic.verpicbackend.domain.preview.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "preview")
public class Preview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String subject;

    @Column
    private String context;

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
    private Preview(String subject, String context, List<DetailTopic> detailTopicList, List<Expression> expressionList) {
        this.subject = subject;
        this.context = context;
        this.detailTopicList = detailTopicList;
        this.expressionList = expressionList;
    }
}
