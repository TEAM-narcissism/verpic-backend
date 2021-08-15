package teamverpic.verpicbackend.domain.reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;

import java.util.List;

public interface StudyReservationRepository extends JpaRepository<StudyReservation, Long> {


    public List<StudyReservation> findByTopicIdAndUserId(Long topicId, Long userId);
}
