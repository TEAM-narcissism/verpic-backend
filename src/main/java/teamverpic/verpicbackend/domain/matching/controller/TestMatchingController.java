package teamverpic.verpicbackend.domain.matching.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import teamverpic.verpicbackend.domain.matching.service.MatchService;
import teamverpic.verpicbackend.domain.reservation.dao.StudyReservationRepository;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;


@Slf4j
@RequiredArgsConstructor
@Controller
public class TestMatchingController {

    private final MatchService matchingService;
    private final StudyReservationRepository reservationRepository;

    @PostMapping("/matching")
    @ResponseBody
    public String testMatching() {
        matchingService.match(reservationRepository.findAll());
        return "ok";
    }
}
