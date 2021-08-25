package teamverpic.verpicbackend.domain.matching.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.domain.matching.dao.MatchRepository;
import teamverpic.verpicbackend.domain.matching.dao.MatchUserRepository;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;
import teamverpic.verpicbackend.domain.matching.dto.MatchingResponseDto;
import teamverpic.verpicbackend.domain.matching.dto.ParticipantCheckDto;
import teamverpic.verpicbackend.domain.reservation.dao.StudyReservationRepository;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.reservation.domain.Level;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.dto.UserResponseDto;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MatchService {

    private final UserRepository userRepository;
    private final MatchUserRepository matchUserRepository;
    private final MatchRepository matchRepository;
    private final StudyReservationRepository studyReservationRepository;

    public void match(List<StudyReservation> reservations) {
        // [familiarLanguage][unfamiliarLanguage][level]
        Queue<StudyReservation>[][][] filteredReservations =
                new Queue[Language.values().length][Language.values().length][Level.values().length];

        for (int i = 0; i < Language.values().length; i++) {
            for (int j = 0; j < Language.values().length; j++) {
                for (int k = 0; k < Level.values().length; k++) {
                    filteredReservations[i][j][k] = new LinkedList<>();
                }
            }
        }

        reservations.forEach(reservation -> {
            log.info("reservation userid : {}", reservation.getUser().getId());
            filteredReservations
                    [reservation.getFamiliarLanguage().ordinal()]
                    [reservation.getUnfamiliarLanguage().ordinal()]
                    [reservation.getUserLevel().ordinal()]
                    .add(reservation);
        });

        reservations.forEach(reservation -> {
            if (reservation.isSoldOut()) {
                return;
            }
            // 자신과 매칭되는 조건을 가진 그룹의 큐를 찾는다
            // 현재는 정확히 일치하는 경우만 매칭 가능
            Queue<StudyReservation> matchers = filteredReservations
                    [reservation.getUnfamiliarLanguage().ordinal()]
                    [reservation.getFamiliarLanguage().ordinal()]
                    [reservation.getUserLevel().ordinal()];

            // 그룹에 자신과 매칭가능한 유저가 있는지 확인
            StudyReservation matcher = matchers.poll();
            while (matcher.isSoldOut()) {
                matcher = matchers.poll();
            }

            // 매칭이 가능한 경우 단순히 새로운 match를 생성하고 저장하는 부분
            if (matcher != null) {
                System.out.println("MatchService.match");
                Match match = Match.builder()
                        .build();
                
                User reservationUser = reservation.getUser();
                User matcherUser = matcher.getUser();
                MatchUser matchUser1 = MatchUser.builder().user(reservationUser).match(match).reservation(reservation).build();
                MatchUser matchUser2 = MatchUser.builder().user(matcherUser).match(match).reservation(matcher).build();

                match.addParticipants(new ArrayList<>(Arrays.asList(matchUser1, matchUser2)));
                reservationUser.addUserMatch(matchUser1);
                matcherUser.addUserMatch(matchUser2);

                reservation.setSoldOut(true);
                matcher.setSoldOut(true);
                studyReservationRepository.save(reservation);
                studyReservationRepository.save(matcher);
                matchRepository.save(match);
                matchUserRepository.save(matchUser1);
                matchUserRepository.save(matchUser2);
                userRepository.save(reservationUser);
                userRepository.save(matcherUser);
            }
        });
    }


    public Match findByMatchId(String matchId) {

        return matchRepository.findById(Long.parseLong(matchId)).orElseThrow(
                () -> new IllegalArgumentException("해당 매치가 존재하지 않아요")
        );
    }

    public List<MatchingResponseDto> findByUserId(Long userId) {
        List<MatchUser> matchUsers = matchUserRepository.findByUserId(userId);

        return matchUsers.stream().map(
                matchUser -> new MatchingResponseDto(matchUser)
        ).collect(Collectors.toList());
    }

    public List<MatchingResponseDto> findByUserEmail(String email) {
        List<MatchUser> matchUsers = matchUserRepository.findByUserEmail(email);

        return matchUsers.stream().map(
                matchUser -> new MatchingResponseDto(matchUser)
        ).collect(Collectors.toList());
    }

    public List<UserResponseDto> findUserByMatchId(Long matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(
                () -> new IllegalArgumentException("해당 매치가 존재하지 않아요.")
        );

        return match.getParticipants().stream().map(
                matchUser -> new UserResponseDto(matchUser.getUser())
        ).collect(Collectors.toList());
    }

    public ParticipantCheckDto isParticipant(Long matchId, Long userId) {
        List<UserResponseDto> userResponseDtos = findUserByMatchId(matchId);

        boolean check = false;

        for(UserResponseDto userResponseDto : userResponseDtos) {
            if(userResponseDto.getId() == userId) {
                check = true;
                break;
            }
        }

        return new ParticipantCheckDto(check);
    }
}
