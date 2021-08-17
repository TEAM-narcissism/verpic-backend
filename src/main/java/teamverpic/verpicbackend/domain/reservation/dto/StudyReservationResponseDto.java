package teamverpic.verpicbackend.domain.reservation.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.reservation.domain.Level;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.topic.domain.Topic;
import teamverpic.verpicbackend.domain.user.domain.User;

import javax.persistence.*;

@Data
@Getter
@RequiredArgsConstructor
public class StudyReservationResponseDto {

    private Long id;
    private Language familiarLanguage;
    private Language unfamiliarLanguage;
    private Level userLevel;
    private int startTime;
    private Topic topic;
    private boolean isSoldOut;

    public StudyReservationResponseDto(StudyReservation studyReservation) {
        this.id = studyReservation.getId();
        this.familiarLanguage = studyReservation.getFamiliarLanguage();
        this.unfamiliarLanguage = studyReservation.getUnfamiliarLanguage();
        this.userLevel = studyReservation.getUserLevel();
        this.startTime = studyReservation.getStartTime();
        this.topic =studyReservation.getTopic();
        this.isSoldOut = studyReservation.isSoldOut();
    }


}
