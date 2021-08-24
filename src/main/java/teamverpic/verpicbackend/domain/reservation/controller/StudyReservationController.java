package teamverpic.verpicbackend.domain.reservation.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.reservation.dto.StudyReservationResponseDto;
import teamverpic.verpicbackend.domain.reservation.service.StudyReservationService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value="/reservation")
public class StudyReservationController {

    private final StudyReservationService studyReservationService;

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String reservation(
            Authentication authentication,
            @RequestBody Map<String, String> reservation){

        studyReservationService.registerReservation(authentication.getName(), reservation);
        return "스터디 예약 성공";
    }

    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public String cancelReservation(Authentication authentication, @PathVariable Long reservationId){
        studyReservationService.deleteReservation(reservationId, authentication.getName());
        return "스터디 취소 성공";
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyReservationResponseDto> findReservationByUserEmail(Authentication authentication) {
        return studyReservationService.findReservationsByUser(authentication.getName());
    }

}
