package teamverpic.verpicbackend.domain.topic.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

@Data
public class TopicDto {

    private String message;
    private HttpStatus httpStatus;
    private Topic topic;

}
