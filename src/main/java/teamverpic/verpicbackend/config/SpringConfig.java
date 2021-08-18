package teamverpic.verpicbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SpringConfig {

    @Bean
    public Map<Long, Set<String>> getRoomId2SessionIDs() {
        Map<Long, Set<String>> roomId2SessionIDs = new ConcurrentHashMap<>();
        return roomId2SessionIDs;
    }

    @Bean
    public Map<String, Long> getSessionId2RoomId() {
        Map<String, Long> sessionId2RoomId = new ConcurrentHashMap<>();
        return sessionId2RoomId;
    }
}
