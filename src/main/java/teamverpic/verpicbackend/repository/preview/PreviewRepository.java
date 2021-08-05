package teamverpic.verpicbackend.repository.preview;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.preview.Preview;

public interface PreviewRepository extends JpaRepository<Preview, Long> {
}
