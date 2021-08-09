package teamverpic.verpicbackend.domain.matching.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.webservices.client.AutoConfigureMockWebServiceServer;
import org.springframework.boot.test.autoconfigure.webservices.client.WebServiceClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import teamverpic.verpicbackend.domain.matching.dao.MatchRepository;
import teamverpic.verpicbackend.domain.matching.dao.MatchUserRepository;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.reservation.dao.StudyReservationRepository;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.reservation.domain.Level;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@AutoConfigureMockMvc
@AutoConfigureMockWebServiceServer
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
class MatchServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatchUserRepository matchUserRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private StudyReservationRepository studyReservationRepository;
    @Autowired
    private MatchService matchService;

    @Test
    void match() {
        StudyReservation reservation1 = StudyReservation.builder()
                .familiarLanguage(Language.ENG)
                .unfamiliarLanguage(Language.KOR)
                .userLevel(Level.BEGINNER)
                .userId(1l)
                .isSoldOut(false)
                .build();

        StudyReservation reservation2 = StudyReservation.builder()
                .familiarLanguage(Language.KOR)
                .unfamiliarLanguage(Language.ENG)
                .userLevel(Level.BEGINNER)
                .userId(2l)
                .isSoldOut(false)
                .build();

        System.out.println("reservation2.getUnfamiliarLanguage() = " + reservation2.getUnfamiliarLanguage());

        matchService.match(new ArrayList<>(Arrays.asList(reservation1, reservation2)));

        List<Match> all = matchRepository.findAll();

        all.forEach(a -> {
            System.out.println(a.toString());
        });
    }
}