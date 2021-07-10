package teamverpic.verpicbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import teamverpic.verpicbackend.repository.UserRepository;

@Configuration
public class SpringConfig {

    private final UserRepository userRepository;

    @Autowired
    public SpringConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
