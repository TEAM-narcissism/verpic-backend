package teamverpic.verpicbackend.config.security.dto;

import lombok.Getter;
import teamverpic.verpicbackend.domain.user.domain.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getFirstName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
