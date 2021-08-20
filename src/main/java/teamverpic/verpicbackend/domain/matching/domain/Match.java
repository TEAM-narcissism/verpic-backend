package teamverpic.verpicbackend.domain.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamverpic.verpicbackend.domain.feedback.Feedback;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<AudioFile> audioFileList;

    public void addParticipants(List<MatchUser> participants) {
        participants.forEach(participant -> {
            this.participants.add(participant);
        });
    }

    public void addAudioFile(AudioFile audioFile) {
        audioFile.setMatch(this);
        this.audioFileList.add(audioFile);
    }
}
