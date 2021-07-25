package teamverpic.verpicbackend.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@RequiredArgsConstructor
@Setter
public class WebSocketMessage {
    private String from;
    private String type;
    private String data;
    private Object candidate;
    private Object sdp;

    public String getFrom() {
        return from;
    }
    public String getType() {
        return type;
    }
    public String getData() {
        return data;
    }
    public Object getCandidate() {
        return candidate;
    }
    public Object getSdp() {
        return sdp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebSocketMessage message = (WebSocketMessage) o;
        return Objects.equals(getFrom(), message.getFrom()) &&
                Objects.equals(getType(), message.getType()) &&
                Objects.equals(getData(), message.getData()) &&
                Objects.equals(getCandidate(), message.getCandidate()) &&
                Objects.equals(getSdp(), message.getSdp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getType(), getData(), getCandidate(), getSdp());
    }

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "from='" + from + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", candidate=" + candidate +
                ", sdp=" + sdp +
                '}';
    }



}
