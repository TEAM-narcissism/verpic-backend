package teamverpic.verpicbackend.domain.user.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import teamverpic.verpicbackend.domain.user.exception.CustomNullPointerException;
import teamverpic.verpicbackend.domain.user.dto.HttpResponseDto;

import java.nio.charset.Charset;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomNullPointerException.class)
    public ResponseEntity<HttpResponseDto> handler(CustomNullPointerException e){
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(),httpResponseDto.getHttpStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<HttpResponseDto> handler(NullPointerException e){
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(),httpResponseDto.getHttpStatus());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpResponseDto> handler(IllegalArgumentException e){
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(),httpResponseDto.getHttpStatus());
    }


    public HttpHeaders getHttpHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }



}
