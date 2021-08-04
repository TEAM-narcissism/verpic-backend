package teamverpic.verpicbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import teamverpic.verpicbackend.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class SpringConfig {

    private final UserRepository userRepository;

    @Autowired
    public SpringConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public Map<Long, Set<String>> getRoomId2SessionIDs() {
        Map<Long, Set<String>> roomId2SessionIDs = new HashMap<>();
        return roomId2SessionIDs;
    }

    @Bean
    public Map<String, Long> getSessionId2RoomId() {
        Map<String, Long> sessionId2RoomId = new HashMap<>();
        return sessionId2RoomId;
    }
}
