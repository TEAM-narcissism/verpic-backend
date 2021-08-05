package teamverpic.verpicbackend.domain.preview.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "detail_topic")
public class DetailTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String context;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "detail_topic")
    private List<UserAnswer> userAnswerList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "preview_id")
    private Preview preview;

    @Builder
    private DetailTopic(String context, List<UserAnswer> userAnswerList) {
        this.context = context;
        this.userAnswerList = userAnswerList;
    }

    public void addUserAnswer(UserAnswer userAnswer) {
        userAnswer.setDetail_topic(this);
        this.userAnswerList.add(userAnswer);
    }
}
