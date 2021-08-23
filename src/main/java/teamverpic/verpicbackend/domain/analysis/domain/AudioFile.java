package teamverpic.verpicbackend.domain.analysis.domain;

import lombok.*;
import teamverpic.verpicbackend.domain.matching.domain.Match;
import teamverpic.verpicbackend.domain.reservation.domain.Language;
import teamverpic.verpicbackend.domain.user.domain.User;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class AudioFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long audioFileId;

    private String fileDir;
    private String fileName;
    private Language lang;
    private Integer sessionOrder;
    private boolean merged;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @OneToOne
    @JoinColumn(name = "script_id")
    private Script script;

    public void setScript(Script script) {
        script.setAudioFile(this);
        this.script = script;
    }
}
