package teamverpic.verpicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.dto.UserCRUDDto;
import teamverpic.verpicbackend.repository.UserRepository;
import teamverpic.verpicbackend.service.UserService;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Controller
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserCRUDDto> join(@RequestBody Map<String, String> user) {
        UserCRUDDto body = new UserCRUDDto();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            userService.join(user, passwordEncoder);
        } catch(Exception e) {
            if(e instanceof IllegalStateException) {
                body.setMessage("중복 이메일 가입 불가");
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
            else if(e instanceof ParseException) {
                body.setMessage("생일 날짜 형식 오류");
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
        }
        body.setMessage("회원가입 성공");
        return new ResponseEntity<>(body, headers, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserCRUDDto> login(@RequestBody Map<String, String> user) {
        UserCRUDDto body = new UserCRUDDto();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        String token = "";
        try{
            token = userService.login(user, passwordEncoder, jwtTokenProvider);
        }catch(Exception e) {
            if(e.getMessage().equals("가입되지 않은 E-MAIL 입니다.") || e.getMessage().equals("잘못된 비밀번호입니다.")) {
                body.setMessage("아이디 또는 비밀번호 오류");
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
        }

        body.setMessage("로그인 완료");
        Map data = new HashMap<String, Object>(); // 로그인 DTO 별도 지정 필요? 현재:(Token : ~)
        data.put("Token", token);
        body.setData(data);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
