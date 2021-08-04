package teamverpic.verpicbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.StudyReservation;

public interface StudyReservationRepository extends JpaRepository<StudyReservation, Long> {
}
