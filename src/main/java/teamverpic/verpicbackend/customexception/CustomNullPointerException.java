package teamverpic.verpicbackend.customexception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import teamverpic.verpicbackend.dto.HttpResponseDto;


@Getter
@Setter
public class CustomNullPointerException extends NullPointerException {

    public CustomNullPointerException(HttpResponseDto responseDto, HttpHeaders httpHeaders){
        this.responseDto = responseDto;
        this.httpHeaders = httpHeaders;
    }

    private HttpResponseDto responseDto;
    private HttpHeaders httpHeaders;

}
