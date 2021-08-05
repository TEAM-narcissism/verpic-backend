package teamverpic.verpicbackend.domain.topic.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.topic.domain.Day;
import teamverpic.verpicbackend.domain.topic.domain.Topic;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findAllByStudyDay(Day day);
    List<Topic> findAll();
}
