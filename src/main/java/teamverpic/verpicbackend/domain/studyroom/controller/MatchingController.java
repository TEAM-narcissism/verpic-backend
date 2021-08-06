package teamverpic.verpicbackend.domain.studyroom.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import teamverpic.verpicbackend.domain.studyroom.service.MatchingService;

@Controller
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

//    @GetMapping({"", "/", "/index", "/home", "/main"})
    @GetMapping({"/index", "/home", "/main"})
    public ModelAndView displayMainPage(Long id, String uuid) {
        return matchingService.displayMainPage(id, uuid);
    }

    @PostMapping(value= "/room", params = "action=create")
    public ModelAndView processRoomSelection(@ModelAttribute("id") String sid,
                                             @ModelAttribute("uuid") String uuid,
                                             BindingResult binding) {
        return matchingService.processRoomSelection(sid, uuid, binding);
    }

    @GetMapping("/room/{sid}/user/{uuid}")
    public ModelAndView displaySelectedRoom(@PathVariable("sid") String sid,
                                            @PathVariable("uuid") String uuid) {
        return matchingService.displaySelectedRoom(sid, uuid);
    }

    @GetMapping("/room/{sid}/user/{uuid}/exit")
    public ModelAndView processRoomExit(@PathVariable("sid") String sid, @PathVariable("uuid")  String uuid) {
        return matchingService.processRoomExit(sid, uuid);
    }

    @GetMapping("/room/random")
    public ModelAndView requestRandomRoomNumber(@ModelAttribute("uuid") String uuid) {
        return matchingService.requestRandomRoomNumber(uuid);
    }

    @GetMapping("/offer")
    public ModelAndView displaySampleSdpOffer() {
        return new ModelAndView("sdp_offer");
    }

    @GetMapping("/stream")
    public ModelAndView displaySampleStreaming() {
        return new ModelAndView("streaming");
    }

}
