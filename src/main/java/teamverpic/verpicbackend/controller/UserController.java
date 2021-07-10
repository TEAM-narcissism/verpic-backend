package teamverpic.verpicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.repository.UserRepository;
import teamverpic.verpicbackend.service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user) throws ParseException {
        return userService.join(user);
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        return userService.login(user);
    }
}
