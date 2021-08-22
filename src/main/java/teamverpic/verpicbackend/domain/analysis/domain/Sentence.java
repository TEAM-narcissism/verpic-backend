package teamverpic.verpicbackend.domain.analysis.domain;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sentenceId;

    private String sentence;
    private double startSecond;

    @ManyToOne
    @JoinColumn(name = "script_id")
    private Script script;
}
