package teamverpic.verpicbackend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.config.security.WebSecurityConfig;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.repository.MemoryUserRepository;
import teamverpic.verpicbackend.security.CustomUserDetailService;
import teamverpic.verpicbackend.service.UserService;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    UserService userService;
    MemoryUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void beforeEach() {
        userRepository = new MemoryUserRepository();
        userService = new UserService(userRepository);
        jwtTokenProvider = new JwtTokenProvider(new CustomUserDetailService(userRepository));
        passwordEncoder = new WebSecurityConfig(jwtTokenProvider).passwordEncoder();
    }

    @AfterEach
    public void afterEach() {
        userRepository.clearStore();
    }

    @Test
    void 회원가입() {
        Map<String, String> user = new HashMap<>();
        user.put("email", "yk0318ha@gmail.com");
        user.put("password", "kimyoungi99");
        user.put("birthDate", "1999/03/18");
        user.put("firstName", "Young-Ki");
        user.put("lastName", "Kim");


        assertDoesNotThrow(() -> userService.join(user, passwordEncoder));

        User findUser = userService.findUser(user.get("email")).get();
        assertThat(user.get("email")).isEqualTo(findUser.getEmail());
        assertThat(user.get("password")).isNotEqualTo(findUser.getPassword()); // 암호화 확인
    }

    @Test
    void 중복가입() {
        Map<String, String> user1 = new HashMap<>();
        user1.put("email", "yk0318ha@gmail.com");
        user1.put("password", "kimyoungi99");
        user1.put("birthDate", "1999/03/18");
        user1.put("firstName", "Young-Ki");
        user1.put("lastName", "Kim");

        Map<String, String> user2 = new HashMap<>();
        user2.put("email", "yk0318ha@gmail.com");
        user2.put("password", "kimyoungi99");
        user2.put("birthDate", "1999/03/18");
        user2.put("firstName", "Young-Ki");
        user2.put("lastName", "Kim");

        assertDoesNotThrow(() -> userService.join(user1, passwordEncoder));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(user2, passwordEncoder));

        assertThat(e.getMessage()).isEqualTo("이미 가입된 이메일입니다.");
    }

    @Test
    void 생일형식오류() {
        Map<String, String> user1 = new HashMap<>();
        user1.put("email", "yk0318ha@gmail.com");
        user1.put("password", "kimyoungi99");
        user1.put("birthDate", "1999-03-18");
        user1.put("firstName", "Young-Ki");
        user1.put("lastName", "Kim");

        assertThrows(ParseException.class, () -> userService.join(user1, passwordEncoder));
    }
}
