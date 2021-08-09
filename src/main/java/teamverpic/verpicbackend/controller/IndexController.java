package teamverpic.verpicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import teamverpic.verpicbackend.config.security.dto.SessionUser;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final HttpSession httpSession;

    //Main Page
    @GetMapping({"", "/"})
    public String mainPage() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user == null) {
            System.out.println("user is null ");
        }
        System.out.println("user email is = " + user.getEmail());
        return "redirect:/oauth2-login/get-jwt";
    }
}
