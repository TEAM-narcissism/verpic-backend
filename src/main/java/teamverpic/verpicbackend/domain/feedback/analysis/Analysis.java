package teamverpic.verpicbackend.domain.feedback.analysis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamverpic.verpicbackend.domain.feedback.Feedback;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "analysis")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

}
