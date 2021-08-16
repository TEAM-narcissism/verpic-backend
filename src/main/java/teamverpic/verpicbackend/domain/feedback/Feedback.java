package teamverpic.verpicbackend.domain.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamverpic.verpicbackend.domain.feedback.analysis.Analysis;
import teamverpic.verpicbackend.domain.feedback.script.Script;
import teamverpic.verpicbackend.domain.feedback.vocabulary.Vocabulary;
import teamverpic.verpicbackend.domain.matching.domain.Match;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feedback")
    private List<Script> scripts;

    public void addScript(Script script) {
        script.setFeedback(this);
        this.scripts.add(script);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feedback")
    private List<Vocabulary> vocabularies;

    public void addVocabulary(Vocabulary vocabulary) {
        vocabulary.setFeedback(this);
        this.vocabularies.add(vocabulary);
    }

    private List<Analysis> analyses;

    public void addAnalysis(Analysis analysis) {
        analysis.setFeedback(this);
        this.analyses.add(analysis);
    }

    @Builder
    public Feedback(Match match, List<Script> scripts, List<Vocabulary> vocabularies, List<Analysis> analyses){
        this.match = match;
        this.scripts = scripts;
        this.vocabularies = vocabularies;
        this.analyses = analyses;
    }
}
