package teamverpic.verpicbackend.common.response;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter @Getter
public class HttpResponseDto {
    private StatusEnum httpStatus;
    private String message;
    private Object data;
}
