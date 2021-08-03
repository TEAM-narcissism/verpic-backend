package teamverpic.verpicbackend.controller;

import com.mysql.cj.xdevapi.JsonParser;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamverpic.verpicbackend.domain.StudyReservation;
import teamverpic.verpicbackend.dto.StudyReservationDto;
import teamverpic.verpicbackend.dto.TopicDto;
import teamverpic.verpicbackend.service.StudyReservationService;

import java.nio.charset.Charset;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Controller
public class StudyReservationController {

    private final StudyReservationService studyReservationService;

    @CrossOrigin
    @PostMapping("/reservation")
    public ResponseEntity<StudyReservationDto> reservation(
            @RequestBody Map<String, String> reservation){

        StudyReservationDto studyReservationDto=new StudyReservationDto();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            studyReservationService.registerReservation(reservation);
        }
        catch(IllegalStateException e){
            studyReservationDto.setMessage("중복 스터디 예약 불가");
            studyReservationDto.setHttpStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(studyReservationDto, headers, HttpStatus.BAD_REQUEST);
        }

        studyReservationDto.setMessage("스터디 예약 성공");
        studyReservationDto.setHttpStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(studyReservationDto, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/reservation/{reservationId}")
    public ResponseEntity<StudyReservationDto> cancelReservation(@PathVariable Long reservationId){
        StudyReservationDto studyReservationDto=new StudyReservationDto();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            studyReservationService.deleteReservation(reservationId);
        }
        catch(Exception e){
            studyReservationDto.setMessage("취소할 스터디가 없음");
            studyReservationDto.setHttpStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(studyReservationDto, headers, HttpStatus.BAD_REQUEST);
        }

        studyReservationDto.setMessage("스터디 취소 성공");
        studyReservationDto.setHttpStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(studyReservationDto, headers, HttpStatus.CREATED);
    }
}
