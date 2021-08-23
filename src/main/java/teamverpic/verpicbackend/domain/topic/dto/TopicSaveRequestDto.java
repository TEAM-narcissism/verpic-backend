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
    private String korTheme;
    private String engTheme;

    @Builder
    public TopicSaveRequestDto(Day studyDay, String korTheme, String engTheme){
//        this.studyDate = studyDate;
        this.studyDay = studyDay;
        this.korTheme = korTheme;
        this.engTheme = engTheme;
    }

    public Topic toEntity() {
        Topic topic = Topic.builder()
//                .studyDate(studyDate)
                .studyDay(studyDay)
//                .theme(theme)
                .korTheme(korTheme)
                .engTheme(engTheme)
                .build();
        return topic;
    }
}
