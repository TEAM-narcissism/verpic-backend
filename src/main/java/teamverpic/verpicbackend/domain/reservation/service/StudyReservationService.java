package teamverpic.verpicbackend.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.reservation.domain.Level;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.reservation.dao.StudyReservationRepository;

import javax.transaction.Transactional;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyReservationService {

    private final StudyReservationRepository studyReservationRepository;

    public Long registerReservation(Map<String, String> reservation){
        return studyReservationRepository.save(StudyReservation.builder()
                .familiarLanguage(Language.valueOf(reservation.get("familiarLanguage")))
                .unfamiliarLanguage(Language.valueOf(reservation.get("unfamiliarLanguage")))
                .userLevel(Level.valueOf(reservation.get("userLevel")))
                .startTime(Integer.parseInt(reservation.get("startTime")))
                .build()).getId();
    }

    public void deleteReservation(Long reservationId){
        studyReservationRepository.deleteById(reservationId);
    }
}
