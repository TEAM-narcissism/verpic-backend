package teamverpic.verpicbackend.domain.matching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.matching.domain.Match;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAll();



    //Match findByReservationId(Long reservationId);
}
