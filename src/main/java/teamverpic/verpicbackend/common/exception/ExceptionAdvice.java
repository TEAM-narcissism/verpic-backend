package teamverpic.verpicbackend.common.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import teamverpic.verpicbackend.common.response.HttpResponseDto;
import teamverpic.verpicbackend.common.response.StatusEnum;

import java.nio.charset.Charset;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomNullPointerException.class)
    public ResponseEntity<HttpResponseDto> handler(CustomNullPointerException e){
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(StatusEnum.BAD_REQUEST);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<HttpResponseDto> handler(NullPointerException e){
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(StatusEnum.BAD_REQUEST);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpResponseDto> handler(IllegalArgumentException e){
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(StatusEnum.UNAUTHORIZED);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    public HttpHeaders getHttpHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }



}
