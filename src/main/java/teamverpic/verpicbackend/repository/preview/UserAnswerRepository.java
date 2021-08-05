package teamverpic.verpicbackend.repository.preview;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.preview.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
}
