package teamverpic.verpicbackend.domain.analysis.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.analysis.domain.Script;

public interface ScriptRepository extends JpaRepository<Script, Long> {
}
