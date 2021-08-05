package teamverpic.verpicbackend.repository.preview;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.preview.Expression;

public interface ExpressionRepository extends JpaRepository<Expression, Long> {
}
