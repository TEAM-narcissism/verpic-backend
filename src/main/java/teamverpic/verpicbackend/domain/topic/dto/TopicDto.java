package teamverpic.verpicbackend.domain.topic.dto;

import lombok.*;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.util.Date;

@Getter
public class TopicDto {
    private Date studyDate;
    private Day studyDay;
    private String theme;

    public TopicDto(Topic topic){
        this.studyDate=topic.getStudyDate();
        this.studyDay=topic.getStudyDay();
        this.theme=topic.getTheme();
    }
}
