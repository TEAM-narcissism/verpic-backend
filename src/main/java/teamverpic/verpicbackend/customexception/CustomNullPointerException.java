package teamverpic.verpicbackend.customexception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import teamverpic.verpicbackend.dto.HttpResponseDto;



public class CustomNullPointerException extends NullPointerException {

    public CustomNullPointerException(String msg){
        super(msg);
    }

}
