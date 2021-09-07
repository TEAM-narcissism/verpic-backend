package teamverpic.verpicbackend.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.matching.service.MatchService;
import teamverpic.verpicbackend.domain.reservation.dao.StudyReservationRepository;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administration/matching")
public class AdminMatchingController {

    private final StudyReservationRepository studyReservationRepository;
    private final MatchService matchService;

    @GetMapping("/{topicId}")
    public String startMatching(@PathVariable Long topicId) {
        int[] startTimes = {17, 18, 19};
        for (int startTime : startTimes) {
            matchService.match(studyReservationRepository.findAllByTopicIdAndStartTime(topicId, startTime));
        }

        return "redirect:/administration/topics/";
    }
}
