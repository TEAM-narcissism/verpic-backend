package teamverpic.verpicbackend.config.security.dto;

import lombok.Builder;
import lombok.Getter;
import teamverpic.verpicbackend.domain.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        return User.builder()
                .firstName(name)
                .email(email)
                .picture(picture)
                .roles(roles)
                .password("a;kdnfvierpoivnapiojv")
                .build();
    }
}
