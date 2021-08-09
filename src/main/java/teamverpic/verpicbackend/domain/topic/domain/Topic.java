package teamverpic.verpicbackend.domain.topic.domain;

import lombok.*;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.topic.domain.Day;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date studyDate;

    @Enumerated(EnumType.STRING)
    private Day studyDay;

    private String theme;

    private int numOfParticipant;

//    @Embedded
//    private Image image;

    @Column(nullable = false)
    private String origImgName;

    @Column(nullable = false)
    private String imgName;

    @Column(nullable = false)
    private String imgPath;

    @OneToOne
    @JoinColumn(name = "preview_id")
    private Preview preview;
}
