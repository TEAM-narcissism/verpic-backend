package teamverpic.verpicbackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    // 프로필 설정 시 추가 (detail_profile)
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String firstLanguage;
    private String learnLanguage;
//    private String availableLanguage;
//    private String hobby;
//    private String interest;

    @Builder
    public UserUpdateRequestDto(String firstName, String lastName, Date birthDate,
                                String firstLanguage, String learnLanguage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.firstLanguage = firstLanguage;
        this.learnLanguage = learnLanguage;
    }
}
