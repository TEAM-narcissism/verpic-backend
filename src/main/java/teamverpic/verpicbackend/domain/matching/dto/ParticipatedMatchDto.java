package teamverpic.verpicbackend.domain.matching.dto;

import lombok.Data;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.util.Date;

@Data
public class ParticipatedMatchDto {

    private Long matchId;
    private String korTheme;
    private String engTheme;
    // ms
    private Long date;

    public ParticipatedMatchDto(MatchUser matchUser) {
        StudyReservation reservation = matchUser.getReservation();
        this.matchId = matchUser.getMatch().getId();
        Topic topic = reservation.getTopic();
        this.korTheme = topic.getKorTheme();
        this.engTheme = topic.getKorTheme();
        this.date = topic.getStudyDate().getTime();
    }
}
