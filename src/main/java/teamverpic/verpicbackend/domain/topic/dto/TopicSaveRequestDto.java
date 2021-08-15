package teamverpic.verpicbackend.domain.topic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.util.Date;

@Getter
@NoArgsConstructor
public class TopicSaveRequestDto {
//    private Date studyDate;
    private Day studyDay;
    private String theme;

    @Builder
    public TopicSaveRequestDto(Day studyDay, String theme){
//        this.studyDate = studyDate;
        this.studyDay = studyDay;
        this.theme = theme;
    }

    public Topic toEntity() {
        Topic topic = Topic.builder()
//                .studyDate(studyDate)
                .studyDay(studyDay)
                .theme(theme)
                .build();
        return topic;
    }
}
