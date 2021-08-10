package teamverpic.verpicbackend.domain.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ChatRoom {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Getter
    private Long participantsId1;
    @Getter
    private Long participantsId2;

    public boolean isParticipant(Long userId) {
        return this.participantsId1 == userId || this.participantsId2 == userId;
    }
}
