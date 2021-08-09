package teamverpic.verpicbackend.domain.preview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.preview.domain.Expression;

public interface ExpressionRepository extends JpaRepository<Expression, Long> {
}
