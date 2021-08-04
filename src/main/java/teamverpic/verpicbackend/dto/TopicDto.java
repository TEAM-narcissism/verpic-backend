package teamverpic.verpicbackend.dto;

import com.mysql.cj.protocol.Message;
import lombok.*;
import org.springframework.http.HttpStatus;
import teamverpic.verpicbackend.domain.Topic;

@Data
public class TopicDto {

    private String message;
    private HttpStatus httpStatus;
    private Topic topic;

}
