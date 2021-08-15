package teamverpic.verpicbackend.domain.topic.dto;

import lombok.*;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.util.Date;

@Getter
public class TopicDto {

    private Long id;
    private Date studyDate;
    private Day studyDay;
    private String theme;
    private int numOfParticipant;

    public TopicDto(Topic topic){

        this.id=topic.getId();
        this.studyDate=topic.getStudyDate();
        this.studyDay=topic.getStudyDay();
        this.theme=topic.getTheme();
        this.id = topic.getId();
        this.numOfParticipant = topic.getNumOfParticipant();
    }
}
