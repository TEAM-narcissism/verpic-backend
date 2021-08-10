package teamverpic.verpicbackend.domain.reservation.domain;

import lombok.*;
import teamverpic.verpicbackend.domain.user.domain.User;

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
    private Long id;

    @ElementCollection
    @CollectionTable(name = "study_participants",joinColumns = @JoinColumn(name="participant_id"))
    private Set<User> participants = new HashSet<>();

}
