package teamverpic.verpicbackend.domain.preview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamverpic.verpicbackend.domain.preview.domain.Preview;

import java.util.Optional;

public interface PreviewRepository extends JpaRepository<Preview, Long> {


    Optional<Preview> findById(Long id);
}
