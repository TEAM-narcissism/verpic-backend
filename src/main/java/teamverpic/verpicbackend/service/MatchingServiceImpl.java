package teamverpic.verpicbackend.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import teamverpic.verpicbackend.domain.VideoRoom;
import teamverpic.verpicbackend.util.Parser;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String REDIRECT = "redirect:/";

    private final VideoRoomService videoRoomService;
    private final Parser parser;

    @Override
    public ModelAndView displayMainPage(Long id, String uuid) {
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("id", id);
        modelAndView.addObject("rooms", videoRoomService.getVideoRooms());
        modelAndView.addObject("uuid", uuid);

        return modelAndView;
    }

    @Override
    public ModelAndView processRoomSelection(String sid, String uuid, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            // simplified version, no errors processing
            return new ModelAndView(REDIRECT);
        }
        Optional<Long> optionalId = parser.parseId(sid);
        optionalId.ifPresent(id -> Optional.ofNullable(uuid).ifPresent(name-> videoRoomService.addRoom(new VideoRoom(id))));

        return displayMainPage(optionalId.orElse(null), uuid);
    }

    @Override
    public ModelAndView displaySelectedRoom(String sid, String uuid) {
        ModelAndView modelAndView = new ModelAndView(REDIRECT);

        if(parser.parseId(sid).isPresent()) {
            VideoRoom videoRoom = videoRoomService.findRoomByStringId(sid).orElse(null);
            if(videoRoom != null && uuid != null && !uuid.isEmpty()) {
                logger.debug("User {} is going to join Room # {}", uuid, sid);

                // open the chat room
                modelAndView = new ModelAndView("chat_room", "id", sid);
                modelAndView.addObject("uuid", uuid);
            }
        }

        return modelAndView;
    }

    @Override
    public ModelAndView processRoomExit(String sid, String uuid) {
        if(sid != null && uuid != null) {
            logger.debug("User {} has left Room #{}", uuid, sid);
        }
        return new ModelAndView(REDIRECT);
    }

    @Override
    public ModelAndView requestRandomRoomNumber(String uuid) {
        return displayMainPage(randomValue(), uuid);
    }

    private Long randomValue() {
        return ThreadLocalRandom.current().nextLong(0, 100);
    }
}
