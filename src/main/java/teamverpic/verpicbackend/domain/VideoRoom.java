package teamverpic.verpicbackend.domain;


import com.sun.istack.NotNull;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VideoRoom {

    public VideoRoom(Long id) {
        this.id = id;
    }

    @NotNull
    @Getter
    private Long id;

    @Getter
    private Map<String, String> clients = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;

        if(o == null || getClass() != o.getClass())
            return false;

        VideoRoom videoRoom = (VideoRoom) o;

        return Objects.equals(getId(), videoRoom.getId()) &&
                Objects.equals(getClients(), videoRoom.getClients());

    }

    @Override
    public int hashCode(){
        return Objects.hash(getId(), getClients());
    }

}
