package teamverpic.verpicbackend.domain.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.feedback.Feedback;
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
    @OneToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

}
