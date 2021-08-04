package teamverpic.verpicbackend.domain;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StudyRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ElementCollection
    @CollectionTable(name = "study_participants",joinColumns = @JoinColumn(name="participant_id"))
    private Set<User> participants = new HashSet<>();

}
