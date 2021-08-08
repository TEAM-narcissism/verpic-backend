package teamverpic.verpicbackend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.config.security.dto.SessionUser;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.dto.UserSearchDto;
import teamverpic.verpicbackend.domain.user.dto.HttpResponseDto;
import teamverpic.verpicbackend.domain.user.dto.UserResponseDto;
import teamverpic.verpicbackend.domain.user.dto.UserUpdateRequestDto;
import teamverpic.verpicbackend.domain.user.service.UserService;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final HttpSession httpSession;

    //Main Page
//    @GetMapping("/test")
//    public String mainPage() {
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//        if (user == null) {
//            return "main"; // 원래 메인 페이지
//        }
//        return "redirect:/oauth2-login/get-jwt";
//    }

    // OAuth2 Login
    @GetMapping("/oauth2-login/get-jwt")
    public ResponseEntity<HttpResponseDto> OAuth2_login() {
        HttpHeaders headers= new HttpHeaders();
        HttpResponseDto body = new HttpResponseDto();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        String token = "";
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        token = userService.OAuth2_login(user, jwtTokenProvider);

        System.out.println("user.getEmail() = " + user.getEmail());
        
        body.setMessage("Log in with Google");
        Map data = new HashMap<String, Object>();
        data.put("Token", token);
        body.setData(data);
        body.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<HttpResponseDto> join(@RequestBody Map<String, String> user) {
        HttpResponseDto body = new HttpResponseDto();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            userService.join(user, passwordEncoder);
        }
        catch(Exception e) {
            if(e instanceof IllegalStateException) {
                body.setMessage("중복 이메일 가입 불가");
                body.setHttpStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
            else if(e instanceof ParseException) {
                body.setMessage("생일 날짜 형식 오류");
                body.setHttpStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
        }
        body.setMessage("회원가입 성공");
        body.setHttpStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(body, headers, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<HttpResponseDto> login(@RequestBody Map<String, String> user) {
        HttpResponseDto body = new HttpResponseDto();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        String token = "";

        try{
            token = userService.login(user, passwordEncoder, jwtTokenProvider);
        }catch(Exception e) {
            if(e.getMessage().equals("가입되지 않은 E-MAIL 입니다.") || e.getMessage().equals("잘못된 비밀번호입니다.")) {
                body.setMessage("아이디 또는 비밀번호 오류");
                body.setHttpStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
        }

        body.setMessage("로그인 완료");
        Map data = new HashMap<String, Object>(); // 로그인 DTO 별도 지정 필요? 현재:(Token : ~)
        data.put("Token", token);
        body.setData(data);
        body.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    // 유저 프로필 조회
    @GetMapping("/users/{id}")
    public UserResponseDto profile (@PathVariable Long id) {
        return userService.findById(id);
    }

    // 유저 프로필 수정
    @PutMapping("/users/{id}")
    public Long profile_update (@PathVariable Long id, @RequestBody UserUpdateRequestDto requestDto) {
        return userService.profile_update(id, requestDto);
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<UserSearchDto> search(@RequestParam(value="searchString") String searchString, final Pageable pageable){
        HttpHeaders headers= new HttpHeaders(); // get 방식이므로 header의 content-type을 정해줄 필요가 없음
        UserSearchDto body = new UserSearchDto();

        Page<User> searchResult=userService.searchUser(pageable, searchString);

        body.setMessage("유저 검색 완료");
        Map data = new HashMap<String, Object>();
        data.put("search result", searchResult);
        body.setUsers(data);

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
