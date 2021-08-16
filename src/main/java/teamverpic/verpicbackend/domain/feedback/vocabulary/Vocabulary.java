package teamverpic.verpicbackend.domain.feedback.vocabulary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamverpic.verpicbackend.domain.feedback.Feedback;
import teamverpic.verpicbackend.domain.feedback.script.Script;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "vocabulary")
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<String> repeated_words = new ArrayList<String>();

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

}
