package teamverpic.verpicbackend.domain.matching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamverpic.verpicbackend.domain.matching.dao.MatchRepository;
import teamverpic.verpicbackend.domain.matching.dao.MatchUserRepository;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ServerTimeController {

    private final MatchRepository matchRepository;
    private final MatchUserRepository matchUserRepository;

    @GetMapping("/time/{matchId}")
    public Long getRemainTime(@PathVariable Long matchId) {
        System.out.println("matchId = " + matchId);
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 매치가 없습니다."));
        List<MatchUser> matchUser = matchUserRepository.findByMatchId(matchId);
        StudyReservation reservation = matchUser.get(0).getReservation();

        Date studyDate = reservation.getTopic().getStudyDate();
        Long startTime = Long.valueOf(reservation.getStartTime());

        // 테스트하려면 리턴값 0으로 바꾸고 하세요!
//        return (studyDate.getTime() + (startTime) * 3600000l) - System.currentTimeMillis();
        return 0L;
    }
}
