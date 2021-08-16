package teamverpic.verpicbackend.domain.feedback.script;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamverpic.verpicbackend.domain.feedback.Feedback;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "script")
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

}
