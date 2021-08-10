package teamverpic.verpicbackend.domain.reservation.domain;

import lombok.*;
import teamverpic.verpicbackend.domain.topic.domain.Topic;
import teamverpic.verpicbackend.domain.user.domain.User;

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
    private Long id;

    private Language familiarLanguage;

    private Language unfamiliarLanguage;

    private Level userLevel;

    private int startTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private boolean isSoldOut;
}
