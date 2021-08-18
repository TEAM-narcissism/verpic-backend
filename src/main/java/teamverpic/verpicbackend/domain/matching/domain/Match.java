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
@Builder
@Entity(name = "_MATCH")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Builder.Default
    @OneToMany(mappedBy = "match")
    private Set<MatchUser> participants = Collections.synchronizedSet(new HashSet<>());

    public void addParticipants(List<MatchUser> participants) {
        participants.forEach(participant -> {
            this.participants.add(participant);
        });
    }
}
