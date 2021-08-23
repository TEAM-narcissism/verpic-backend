package teamverpic.verpicbackend.domain.matching.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.reservation.dto.StudyReservationResponseDto;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
public class MatchingResponseDto {


    private Long id;
    private StudyReservationResponseDto studyReservationResponseDto;
    public MatchingResponseDto(MatchUser matchUser) {
        this.id = matchUser.getMatch().getId();
        this.studyReservationResponseDto = new StudyReservationResponseDto(
                matchUser.getReservation()
        );
    }


}
