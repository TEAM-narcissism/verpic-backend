package teamverpic.verpicbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.Day;
import teamverpic.verpicbackend.domain.Topic;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findAllByStudyDay(Day day);
    List<Topic> findAll();
}
