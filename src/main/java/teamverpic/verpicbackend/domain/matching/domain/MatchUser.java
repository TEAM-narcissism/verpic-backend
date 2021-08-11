package teamverpic.verpicbackend.domain.matching.domain;

import lombok.*;
import teamverpic.verpicbackend.domain.user.domain.User;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
public class MatchUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "_match_id")
    private Match match;
}
