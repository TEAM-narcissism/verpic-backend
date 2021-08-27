package teamverpic.verpicbackend.common.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import teamverpic.verpicbackend.common.response.HttpResponseDto;
import teamverpic.verpicbackend.common.response.StatusEnum;
import teamverpic.verpicbackend.domain.user.exception.CustomAuthenticationException;

import java.nio.charset.Charset;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomNullPointerException.class)
    public ResponseEntity<HttpResponseDto> handler(CustomNullPointerException e) {
        System.out.println("e = " + e);
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(StatusEnum.BAD_REQUEST);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<HttpResponseDto> handler(NullPointerException e) {
        System.out.println("e = " + e);
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(StatusEnum.BAD_REQUEST);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpResponseDto> handler(IllegalArgumentException e) {
        System.out.println("e = " + e);
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(StatusEnum.UNAUTHORIZED);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<HttpResponseDto> handler(AuthenticationException e) {
        return errorResponse(e, StatusEnum.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({CustomAuthenticationException.class})
    public ResponseEntity<HttpResponseDto> handler(CustomAuthenticationException e) {
        return errorResponse(e, StatusEnum.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }


    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }

    public ResponseEntity<HttpResponseDto> errorResponse(Exception e, StatusEnum status, HttpStatus httpStatus) {
        System.out.println("e = " + e);
        HttpResponseDto httpResponseDto = new HttpResponseDto();
        httpResponseDto.setMessage(e.getMessage());
        httpResponseDto.setHttpStatus(status);

        return new ResponseEntity<>(httpResponseDto, getHttpHeaders(), httpStatus);
    }
}
