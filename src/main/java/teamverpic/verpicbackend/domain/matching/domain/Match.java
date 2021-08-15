package teamverpic.verpicbackend.domain.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.feedback.analysis.Analysis;
import teamverpic.verpicbackend.domain.feedback.script.Script;
import teamverpic.verpicbackend.domain.feedback.vocabulary.Vocabulary;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "_MATCH")
@Builder
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private StudyReservation reservation;

    @Builder.Default
    @OneToMany(mappedBy = "match")
    private Set<MatchUser> participants = Collections.synchronizedSet(new HashSet<>());

    public void addParticipants(List<MatchUser> participants) {
        participants.forEach(participant -> {
            this.participants.add(participant);
        });
    }

    //STT
    private String record1;

    private String record2;

    //피드백
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "match")
    private List<Script> scripts;

    public void addScript(Script script) {
        script.setMatch(this);
        this.scripts.add(script);
    }

//    private Vocabulary vocabulary;
//
//    private Analysis analysis;
}
