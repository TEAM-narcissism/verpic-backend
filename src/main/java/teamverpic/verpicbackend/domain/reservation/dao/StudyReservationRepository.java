package teamverpic.verpicbackend.domain.reservation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;

public interface StudyReservationRepository extends JpaRepository<StudyReservation, Long> {
}
