package teamverpic.verpicbackend.domain.matching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;

import java.util.List;

public interface MatchUserRepository extends JpaRepository<MatchUser, Long> {
    MatchUser findByUserIdAndMatchId(Long userId, Long matchId);
    List<MatchUser> findByUserId(Long userId);
    List<MatchUser> findByMatchId(Long matchId);
    List<MatchUser> findByUserEmail(String userEmail);

    @Query("select m from matchUser m left join fetch m.match left join fetch m.reservation where m.user.id = :userId")
    List<MatchUser> findByUserIdWithFetchJoin(@Param("userId")Long userId);
}
