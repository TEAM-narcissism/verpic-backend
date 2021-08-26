package teamverpic.verpicbackend.domain.analysis.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.analysis.domain.Sentence;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
}
