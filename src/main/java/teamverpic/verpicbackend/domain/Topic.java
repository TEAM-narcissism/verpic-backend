package teamverpic.verpicbackend.domain;

import lombok.*;

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
    private Long topicId;

    private Date studyDate;

    @Enumerated(EnumType.STRING)
    private Day studyDay;

    private Long previewId;
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
}
