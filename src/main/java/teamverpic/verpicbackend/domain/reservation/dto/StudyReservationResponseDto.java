package teamverpic.verpicbackend.domain.reservation.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.reservation.domain.Level;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.topic.dto.TopicDto;

@Data
@Getter
@RequiredArgsConstructor
public class StudyReservationResponseDto {

    private Long id;
    private Language familiarLanguage;
    private Language unfamiliarLanguage;
    private Level userLevel;
    private int startTime;
    private TopicDto topic;
    private boolean isSoldOut;

    public StudyReservationResponseDto(StudyReservation studyReservation) {
        this.id = studyReservation.getId();
        this.familiarLanguage = studyReservation.getFamiliarLanguage();
        this.unfamiliarLanguage = studyReservation.getUnfamiliarLanguage();
        this.userLevel = studyReservation.getUserLevel();
        this.startTime = studyReservation.getStartTime();
        this.topic = new TopicDto(studyReservation.getTopic());
        this.isSoldOut = studyReservation.isSoldOut();
    }


}
