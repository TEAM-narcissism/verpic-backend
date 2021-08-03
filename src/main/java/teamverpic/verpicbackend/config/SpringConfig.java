package teamverpic.verpicbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import teamverpic.verpicbackend.repository.StudyReservationRepository;
import teamverpic.verpicbackend.repository.TopicRepository;
import teamverpic.verpicbackend.repository.UserRepository;

@Configuration
public class SpringConfig {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final StudyReservationRepository studyReservationRepository;

    @Autowired
    public SpringConfig(UserRepository userRepository,
                        TopicRepository topicRepository,
                        StudyReservationRepository studyReservationRepository) {
        this.userRepository = userRepository;
        this.topicRepository=topicRepository;
        this.studyReservationRepository=studyReservationRepository;
    }
}
