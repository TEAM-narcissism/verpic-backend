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
    private double wpm;

    @OneToOne(mappedBy = "script")
    private AudioFile audioFile;

    @Builder.Default
    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sentence> sentenceList = new ArrayList<>();

    // 최빈단어
    private String muwRankOne;
    private int muwRankOneFreq;
    private String muwRankTwo;
    private int muwRankTwoFreq;
    private String muwRankThree;
    private int muwRankThreeFreq;
    private String muwRankFour;
    private int muwRankFourFreq;
    private String muwRankFive;
    private int muwRankFiveFreq;


    public void addSentence(Sentence sentence) {
        sentence.setScript(this);
        this.sentenceList.add(sentence);
    }
}
