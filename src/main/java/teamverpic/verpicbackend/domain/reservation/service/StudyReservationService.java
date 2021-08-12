package teamverpic.verpicbackend.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.reservation.domain.Level;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.reservation.dao.StudyReservationRepository;
import teamverpic.verpicbackend.domain.topic.dao.TopicRepository;
import teamverpic.verpicbackend.domain.topic.domain.Topic;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.dto.UserResponseDto;
import teamverpic.verpicbackend.domain.user.service.UserService;

import javax.transaction.Transactional;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyReservationService {

    private final StudyReservationRepository studyReservationRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    public Long registerReservation(String userEmail, Map<String, String> reservation){

        Topic topic = topicRepository.getById(Long.parseLong(reservation.get("topicId")));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않아요."));

        topic.setNumOfParticipant(topic.getNumOfParticipant()+1);

        return studyReservationRepository.save(StudyReservation.builder()
                .topic(topic)
                .user(user)
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
