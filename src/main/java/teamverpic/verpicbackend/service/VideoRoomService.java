package teamverpic.verpicbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import teamverpic.verpicbackend.domain.VideoRoom;
import teamverpic.verpicbackend.util.Parser;

import java.util.*;

@Service
public class VideoRoomService {
    private final Parser parser;
    private final Set<VideoRoom> rooms = new TreeSet<>(Comparator.comparing(VideoRoom::getId));

    @Autowired
    public VideoRoomService(Parser parser) {
        this.parser = parser;
    }

    public Set<VideoRoom> getVideoRooms() {
        TreeSet<VideoRoom> defensiveCopy = new TreeSet<>(Comparator.comparing(VideoRoom::getId));
        defensiveCopy.addAll(rooms);
        return defensiveCopy;
    }

    public Boolean addRoom(VideoRoom room) {
        return rooms.add(room);
    }

    public Optional<VideoRoom> findRoomByStringId(String sid) {

        // simple get() because of parser errors handling
        return rooms.stream().filter(r -> r.getId().equals(parser.parseId(sid).get())).findAny();
    }

    public Long getRoomId(VideoRoom room) {
        return room.getId();
    }

    public Map<String, WebSocketSession> getClients(VideoRoom room) {
        return Optional.ofNullable(room)
                .map(r -> Collections.unmodifiableMap(r.getClients()))
                .orElse(Collections.emptyMap());
    }
    public WebSocketSession addClient(VideoRoom  room,  String name, WebSocketSession session) {
        return room.getClients().put(name, session);
    }

    public WebSocketSession removeClientByName(VideoRoom room, String name) {
        return room.getClients().remove(name);
    }




}
