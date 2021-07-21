package teamverpic.verpicbackend.dto;

import lombok.Getter;
import teamverpic.verpicbackend.domain.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatRoomDto {
    @Getter
    private Long roomId;
    private List<ChatMessageDto> messages;
    private static Long idCount = 1l;
    @Getter
    private Set<Long> participantsId = new HashSet<>();

    public static ChatRoomDto create(Long userId1, Long userId2) {
        ChatRoomDto room = new ChatRoomDto();
        room.roomId = idCount++;
        room.participantsId.add(userId1);
        room.participantsId.add(userId2);

        return room;
    }
}
