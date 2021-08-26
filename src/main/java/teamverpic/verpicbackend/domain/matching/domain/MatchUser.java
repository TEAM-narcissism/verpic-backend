package teamverpic.verpicbackend.domain.matching.domain;

import lombok.*;
import teamverpic.verpicbackend.domain.feedback.Feedback;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.user.domain.User;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity(name = "matchUser")
public class MatchUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    //@OneToOne(fetch = FetchType.LAZY)
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private StudyReservation reservation;


    @ManyToOne
    @JoinColumn(name = "_match_id")
    private Match match;

    //STT
    private String record_first;

    private String record_second;

    //피드백
    @OneToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;
}
