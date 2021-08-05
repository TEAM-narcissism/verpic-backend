package teamverpic.verpicbackend.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@Setter @Getter
public class HttpResponseDto {
    private HttpStatus httpStatus;
    private String message;
    private Object data;
}
