package teamverpic.verpicbackend.domain.user.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import teamverpic.verpicbackend.common.response.StatusEnum;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.config.security.dto.SessionUser;
import teamverpic.verpicbackend.domain.user.dto.UserJoinDto;
import teamverpic.verpicbackend.domain.user.dto.UserSearchDto;
import teamverpic.verpicbackend.common.response.HttpResponseDto;
import teamverpic.verpicbackend.domain.user.dto.UserResponseDto;
import teamverpic.verpicbackend.domain.user.dto.UserUpdateRequestDto;
import teamverpic.verpicbackend.domain.user.service.UserService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@PropertySource("classpath:application-oauth.properties")
@RequestMapping("/api")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final Environment environment;

    @PostMapping("/oauth/google")
    public ResponseEntity<HttpResponseDto> exchange(@RequestBody Map<String, String> token) throws GeneralSecurityException, IOException, ParseException {
        HttpResponseDto body = new HttpResponseDto();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        String googleAccessToken = token.get("accessToken");

        String property = environment.getProperty("spring.security.oauth2.client.registration.google.client-id");
        System.out.println("property = " + property);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),  GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(property))
                .build();

        GoogleIdToken idToken = verifier.verify(googleAccessToken);
        String jwtToken = "";
        if(idToken != null) {
            String email = idToken.getPayload().getEmail();
            String locale = (String) idToken.getPayload().get("locale");
            String familyName = (String)idToken.getPayload().get("family_name");
            String givenName = (String)idToken.getPayload().get("given_name");

            System.out.println("email = " + email);

            UserJoinDto userJoinDto = new UserJoinDto(email, googleAccessToken, givenName, familyName);
            jwtToken = userService.oauth_join(userJoinDto, passwordEncoder, jwtTokenProvider);
        }
        else {
            throw new IllegalStateException("구글 로그인 오류.");
        }

        return getHttpResponseDtoResponseEntity(body, headers, jwtToken);
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
                body.setHttpStatus(StatusEnum.BAD_REQUEST);
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
            else if(e instanceof ParseException) {
                body.setMessage("생일 날짜 형식 오류");
                body.setHttpStatus(StatusEnum.BAD_REQUEST);
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
        }
        body.setMessage("회원가입 성공");
        body.setHttpStatus(StatusEnum.OK);
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
                body.setHttpStatus(StatusEnum.BAD_REQUEST);
                return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
            }
        }
        return getHttpResponseDtoResponseEntity(body, headers, token);
    }

    // 유저 프로필 조회
    @GetMapping("/users/{id}")
    public UserResponseDto profile (@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/users/current-login-user")
    public UserResponseDto getLoginUserByToken(Authentication authentication) {
        return userService.findByEmail(authentication.getName());
    }

    // 유저 프로필 수정
    @PutMapping("/users/{id}")
    public Long profile_update (@PathVariable Long id, @RequestBody UserUpdateRequestDto requestDto) {
        return userService.profile_update(id, requestDto);
    }

    // 검색
    @GetMapping("/search")
    public Page<UserSearchDto> search(@RequestParam(value="searchString") String searchString,
                                      final Pageable pageable){
        Page<UserSearchDto> searchResult=userService.searchUser(pageable, searchString);
        return searchResult;
    }

    private ResponseEntity<HttpResponseDto> getHttpResponseDtoResponseEntity(HttpResponseDto body, HttpHeaders headers, String jwtToken) {
        body.setMessage("로그인 완료");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("Token", jwtToken);
        body.setData(data);
        body.setHttpStatus(StatusEnum.OK);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
