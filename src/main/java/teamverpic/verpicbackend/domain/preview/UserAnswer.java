package teamverpic.verpicbackend.domain.preview;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long user_id;

    @Column
    private String answer;

    @ManyToOne
    @JoinColumn(name = "detail_topic_id")
    private DetailTopic detail_topic;

    @Builder
    private UserAnswer(Long user_id, String answer) {
        this.user_id = user_id;
        this.answer = answer;
    }

    public void update(String answer) {
        this.answer = answer;
    }
}
