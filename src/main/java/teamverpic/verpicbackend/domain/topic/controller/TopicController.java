package teamverpic.verpicbackend.domain.topic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.reservation.service.StudyReservationService;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.dto.TopicDto;
import teamverpic.verpicbackend.domain.topic.dto.TopicSaveRequestDto;
import teamverpic.verpicbackend.domain.topic.service.TopicService;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Controller
public class TopicController {

    private final TopicService topicService;
    private final StudyReservationService studyReservationService;
    private final UserRepository userRepository;

    @PostMapping("topics")
    public Long save(@RequestBody TopicSaveRequestDto requestDto) {
        return topicService.save(requestDto);
    }

//    @GetMapping("/topic/{day}")
//    public List<TopicDto> showTopics(@PathVariable Day day){
//        return topicService.getTopicsByDay(day);
//    }

    @GetMapping("/topic")
    public List<TopicDto> showEveryTopicLaterThanToday() throws ParseException {
        return topicService.getTopicsLaterThanToday();
    }

    @GetMapping("/topic/reservationList")
    public List<TopicDto> showTopicsByUserIdFromReservationsLaterThanToday(
            Authentication authentication) throws ParseException {
        System.out.println(authentication);
        String email = authentication.getName();
        List<StudyReservation> reservations = studyReservationService.findReservationsByUserEmail(email);
        return topicService.getTopicsByReservationsLaterThanToday(reservations);
    }
}
