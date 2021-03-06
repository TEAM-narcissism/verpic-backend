package teamverpic.verpicbackend.domain.preview.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "expression")
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String word;

    @Column
    private String meaning;

    @Column
    private String example;

    @Column
    private String audios;

    @Column
    private String contentType;

    @Column
    @Lob
    private byte[] pronounce;

    @ManyToOne
    @JoinColumn(name = "preview_id")
    private Preview preview;

    @Builder
    private Expression(String word, String meaning, String example, String audios, String contentType, byte[] pronounce) {
        this.word = word;
        this.meaning = meaning;
        this.example = example;
        this.audios=audios;
        this.contentType=contentType;
        this.pronounce = pronounce;
    }
}
