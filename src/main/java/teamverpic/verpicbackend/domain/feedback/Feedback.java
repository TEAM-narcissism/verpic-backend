package teamverpic.verpicbackend.domain.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamverpic.verpicbackend.domain.feedback.analysis.Analysis;
import teamverpic.verpicbackend.domain.feedback.script.Script;
import teamverpic.verpicbackend.domain.feedback.vocabulary.Vocabulary;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JoinColumn(name = "matchUser_id")
    private MatchUser matchUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feedback")
    private List<Script> scripts = new ArrayList<Script>();

    public void addScript(Script script) {
        script.setFeedback(this);
        this.scripts.add(script);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feedback")
    private List<Vocabulary> vocabularies = new ArrayList<Vocabulary>();

    public void addVocabulary(Vocabulary vocabulary) {
        vocabulary.setFeedback(this);
        this.vocabularies.add(vocabulary);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feedback")
    private List<Analysis> analyses = new ArrayList<Analysis>();

    public void addAnalysis(Analysis analysis) {
        analysis.setFeedback(this);
        this.analyses.add(analysis);
    }

    @Builder
    public Feedback(MatchUser matchUser, List<Script> scripts, List<Vocabulary> vocabularies, List<Analysis> analyses){
        this.matchUser = matchUser;
        this.scripts = scripts;
        this.vocabularies = vocabularies;
        this.analyses = analyses;
    }
}
