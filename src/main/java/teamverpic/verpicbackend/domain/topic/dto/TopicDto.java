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
    private String korTheme;
    private String engTheme;
    private int numOfParticipant;
    private String photos;
    private String contentType;
    private byte[] data;

    public TopicDto(Topic topic){

        this.id=topic.getId();
        this.studyDate=topic.getStudyDate();
        this.studyDay=topic.getStudyDay();
        this.korTheme=topic.getKorTheme();
        this.engTheme=topic.getEngTheme();
        this.id = topic.getId();
        this.numOfParticipant = topic.getNumOfParticipant();
        this.photos=topic.getPhotos();
        this.contentType=topic.getContentType();
        this.data=topic.getData();
    }
}
