package teamverpic.verpicbackend.dto;

import lombok.Getter;
import teamverpic.verpicbackend.domain.User;

import java.util.Date;

@Getter
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private String password;

    // 프로필 설정 시 추가 (detail_profile)
    private String nation;
    private String gender;
    private String firstLanguage;
    private String availableLanguage;
    private String learnLanguage;
    private String hobby;
    private String interest;

    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.birthDate = entity.getBirthDate();
        this.email = entity.getEmail();
        this.password = entity.getPassword();

        this.nation = entity.getNation();
        this.gender = entity.getGender();
        this.firstLanguage = entity.getFirstLanguage();
        this.availableLanguage = entity.getAvailableLanguage();
        this.learnLanguage = entity.getLearnLanguage();
        this.hobby = entity.getHobby();
        this.interest = entity.getInterest();
    }
}
