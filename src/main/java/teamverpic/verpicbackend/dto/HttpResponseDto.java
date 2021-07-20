package teamverpic.verpicbackend.dto;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HttpResponseDto {
    private HttpStatus httpStatus;
    private String message;
    private Object data;
}
