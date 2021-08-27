package teamverpic.verpicbackend.domain.reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;

import java.util.List;

public interface StudyReservationRepository extends JpaRepository<StudyReservation, Long> {

    List<StudyReservation> findByTopicIdAndUserId(Long topicId, Long userId);
    List<StudyReservation> findByUserId(Long userId);
    @Query("select r from StudyReservation r left join fetch r.topic where r.user.email = :userEmail")
    List<StudyReservation> findByUserEmail(@Param("userEmail")String userEmail);
}
