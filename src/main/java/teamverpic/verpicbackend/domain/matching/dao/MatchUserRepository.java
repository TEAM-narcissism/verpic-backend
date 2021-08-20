package teamverpic.verpicbackend.domain.matching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;

public interface MatchUserRepository extends JpaRepository<MatchUser, Long> {
    MatchUser findByUserIdAndMatchId(Long userId, Long matchId);
}
