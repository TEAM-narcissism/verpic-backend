package teamverpic.verpicbackend.domain.preview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.preview.domain.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
}
