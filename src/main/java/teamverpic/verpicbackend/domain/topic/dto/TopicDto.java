package teamverpic.verpicbackend.domain.topic.dto;

import lombok.*;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.util.Date;

@Getter
public class TopicDto {

    private Long topicId;
    private Date studyDate;
    private Day studyDay;
    private Long previewId;
    private String theme;
    private int numOfParticipant;

    public TopicDto(Topic topic){
        this.topicId=topic.getTopicId();
        this.studyDate=topic.getStudyDate();
        this.studyDay=topic.getStudyDay();
        this.previewId=topic.getPreviewId();
        this.theme=topic.getTheme();
        this.numOfParticipant=topic.getNumOfParticipant();
    }

}
