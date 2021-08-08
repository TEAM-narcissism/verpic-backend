package teamverpic.verpicbackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

    // 프로필 설정 시 추가 (detail_profile)
    private String firstLanguage;
    private String learnLanguage;
//    private String availableLanguage;
//    private String hobby;
//    private String interest;

    @Builder
    public UserUpdateRequestDto(String firstLanguage, String learnLanguage) {
        this.firstLanguage = firstLanguage;
        this.learnLanguage = learnLanguage;
    }
}
