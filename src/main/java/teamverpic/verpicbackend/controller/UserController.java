package teamverpic.verpicbackend.controller;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.domain.User;

import teamverpic.verpicbackend.dto.UserSearchDto;
import teamverpic.verpicbackend.repository.UserRepository;

import teamverpic.verpicbackend.dto.HttpResponseDto;
import teamverpic.verpicbackend.dto.UserResponseDto;
import teamverpic.verpicbackend.dto.UserUpdateRequestDto;

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
    @GetMapping("/{id}")
    public UserResponseDto profile (@PathVariable Long id) {
        return userService.findById(id);
    }

    // 유저 프로필 수정
    @PutMapping("/{id}")
    public Long profile_update (@PathVariable Long id, @RequestBody UserUpdateRequestDto requestDto) {
        return userService.profile_update(id, requestDto);
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<UserSearchDto> search(@RequestParam(value="searchString") String searchString,
                           final Pageable pageable){
        UserSearchDto body = new UserSearchDto();
        HttpHeaders headers= new HttpHeaders(); // get 방식이므로 header의 content-type을 정해줄 필요가 없음

        Page<User> searchResult=userService.searchUser(pageable, searchString);

        body.setMessage("유저 검색 완료");
        Map data = new HashMap<String, Object>();
        data.put("search result", searchResult);
        body.setUsers(data);

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

}
