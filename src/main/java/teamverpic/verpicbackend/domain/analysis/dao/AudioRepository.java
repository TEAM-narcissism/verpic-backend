package teamverpic.verpicbackend.domain.analysis.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface AudioRepository extends JpaRepository<AudioFile, Long> {

    Optional<AudioFile> findByMatchAndUser(Match match, User user);
}