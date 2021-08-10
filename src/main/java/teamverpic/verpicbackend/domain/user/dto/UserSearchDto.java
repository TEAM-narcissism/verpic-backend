package teamverpic.verpicbackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import teamverpic.verpicbackend.domain.user.domain.User;

import java.util.Date;

@Getter
public class UserSearchDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;

    public UserSearchDto(User user) {
        this.id=user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birthDate = user.getBirthDate();
        this.email = user.getEmail();
    }
}
