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
@Entity
@Builder
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scriptId;
    private int wpm;

    @OneToOne(mappedBy = "script")
    private AudioFile audioFile;

    @Builder.Default
    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sentence> sentenceList = new ArrayList<>();

    public void addSentence(Sentence sentence) {
        sentence.setScript(this);
        this.sentenceList.add(sentence);
    }
}
