package teamverpic.verpicbackend.domain.topic.domain;

import lombok.*;
import teamverpic.verpicbackend.domain.preview.domain.Preview;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = true, length = 64)
    private String photos;

    private String contentType;

    @Lob
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "preview_id")
    private Preview preview;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    private List<StudyReservation> studyReservationList;

    @Builder
    public Topic(Long id, Date studyDate, Day studyDay, String theme, int numOfParticipant,
                 String photos, String contentType, byte[] data) {
        this.id = id;
        this.studyDate = studyDate;
        this.studyDay = studyDay;
        this.theme = theme;
        this.numOfParticipant = numOfParticipant;
        this.photos=photos;
        this.contentType=contentType;
        this.data=data;
    }

    public void addStudyReservation(StudyReservation studyReservation) {
        studyReservation.setTopic(this);
        this.studyReservationList.add(studyReservation);
    }

    @Builder
    public Topic(Long id, Date studyDate, Day studyDay, String theme, int numOfParticipant) {
        this.id = id;
        this.studyDate = studyDate;
        this.studyDay = studyDay;
        this.theme = theme;
        this.numOfParticipant = numOfParticipant;
    }
}
