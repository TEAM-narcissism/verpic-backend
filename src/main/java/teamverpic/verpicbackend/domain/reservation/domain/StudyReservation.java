package teamverpic.verpicbackend.domain.reservation.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StudyReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    private Long userId;
    private Language familiarLanguage;
    private Language unfamiliarLanguage;
    private Level userLevel;

    private Long topicId;

    private int startTime;
    private boolean isSoldOut;
}
