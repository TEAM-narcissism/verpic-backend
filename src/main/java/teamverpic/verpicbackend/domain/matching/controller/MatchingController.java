package teamverpic.verpicbackend.domain.matching.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.matching.dto.MatchingResponseDto;
import teamverpic.verpicbackend.domain.matching.dto.ParticipantCheckDto;
import teamverpic.verpicbackend.domain.matching.service.MatchService;
import teamverpic.verpicbackend.domain.reservation.dao.StudyReservationRepository;



import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/matching")
public class MatchingController {

    private final MatchService matchingService;
    private final StudyReservationRepository reservationRepository;

    @PostMapping
    @ResponseBody
    public String testMatching() {
        matchingService.match(reservationRepository.findAll());
        return "ok";
    }


    @GetMapping("/user/{userId}")
    public List<MatchingResponseDto> findByReservationId(@PathVariable Long userId) {
        return matchingService.findByUserId(userId);
    }

    @GetMapping("/participant-check/{matchId}/{userId}")
    public ParticipantCheckDto isParticipant(@PathVariable Long matchId, @PathVariable Long userId) {
        return matchingService.isParticipant(matchId, userId);
    }

}
