package teamverpic.verpicbackend.domain.user.dto;


import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import java.util.Collections;
import java.util.Date;


@Data
@Getter
public class UserJoinDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private Date birthDate;
    private String firstLanguage;
    private String learnLanguage;


    public UserJoinDto(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }




}
