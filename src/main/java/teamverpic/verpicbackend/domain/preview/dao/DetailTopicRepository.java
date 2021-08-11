package teamverpic.verpicbackend.domain.preview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.preview.domain.DetailTopic;

import java.util.List;

public interface DetailTopicRepository extends JpaRepository<DetailTopic, Long> {

    List<DetailTopic> findByPreviewId(Long id);
}
