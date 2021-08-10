package teamverpic.verpicbackend.domain.preview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.preview.domain.Preview;

public interface PreviewRepository extends JpaRepository<Preview, Long> {
}
