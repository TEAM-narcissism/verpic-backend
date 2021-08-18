package teamverpic.verpicbackend.domain.analysis.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
import teamverpic.verpicbackend.domain.matching.domain.Match;

import java.util.List;

public interface AudioRepository extends JpaRepository<AudioFile, Long> {

    List<AudioFile> findAllByMatch(Match match);
}