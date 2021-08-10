package teamverpic.verpicbackend.domain.preview.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamverpic.verpicbackend.domain.user.domain.User;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "user_answer")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String answer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "detail_topic_id")
    private DetailTopic detail_topic;

    @Builder
    private UserAnswer(String answer) {
        this.answer = answer;
    }

    public void update(String answer) {
        this.answer = answer;
    }
}
