package teamverpic.verpicbackend.domain.reservation.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class StudyReservationDto {
    private String message;
    private HttpStatus httpStatus;
    private Object studyReservation;
}
