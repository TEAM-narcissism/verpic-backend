package teamverpic.verpicbackend.domain.analysis.domain;

import com.google.cloud.speech.v1.SpeechRecognitionResult;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scriptId;

    @OneToOne
    private AudioFile audioFile;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sentence")
    private List<Sentence> sentenceList = new ArrayList<Sentence>();

    public void addSentence(Sentence sentence) {
        sentence.setScript(this);
        this.sentenceList.add(sentence);
    }
}
