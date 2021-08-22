package teamverpic.verpicbackend.domain.matching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;

import java.util.List;

public interface MatchUserRepository extends JpaRepository<MatchUser, Long> {
    MatchUser findByUserIdAndMatchId(Long userId, Long matchId);
    List<MatchUser> findByUserId(Long userId);
    List<MatchUser> findByMatchId(Long matchId);
}
