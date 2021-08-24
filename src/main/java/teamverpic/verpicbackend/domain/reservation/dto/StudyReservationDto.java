package teamverpic.verpicbackend.domain.reservation.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.reservation.domain.Level;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.util.Date;

@Data
@Getter
@RequiredArgsConstructor
public class StudyReservationDto {

    private Long id;
    private String korTheme;
    private String engTheme;
    private Date studyDate;
    private boolean isSoldOut;

    public StudyReservationDto(StudyReservation studyReservation) {
        this.id = studyReservation.getId();
        this.korTheme =studyReservation.getTopic().getKorTheme();
        this.engTheme=studyReservation.getTopic().getEngTheme();
        this.isSoldOut = studyReservation.isSoldOut();
    }
}
