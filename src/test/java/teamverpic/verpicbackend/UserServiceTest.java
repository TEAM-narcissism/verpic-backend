package teamverpic.verpicbackend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import java.text.SimpleDateFormat;
import java.util.*;

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

//    @Test
//    void 유저검색_with_중복제거(){
//        // given
//        Map<String, String> user1 = new HashMap<>();
//        user1.put("email", "zaqxsw1397@gmail.com");
//        user1.put("password", "kimtaehun97");
//        user1.put("birthDate", "1997/04/22");
//        user1.put("firstName", "Taehun");
//        user1.put("lastName", "Kim");
//
//        Map<String, String> user2 = new HashMap<>();
//        user2.put("email", "yk0318ha@gmail.com");
//        user2.put("password", "kimyoungi123!");
//        user2.put("birthDate", "1999/03/18");
//        user2.put("firstName", "Youngki");
//        user2.put("lastName", "Kim");
//
//        Map<String, String> user3 = new HashMap<>();
//        user3.put("email", "h0seoung@gmail.com");
//        user3.put("password", "houseong1999");
//        user3.put("birthDate", "1999/11/17");
//        user3.put("firstName", "Hoseong");
//        user3.put("lastName", "You");
//
//        Map<String, String> user4 = new HashMap<>();
//        user4.put("email", "nokchabou@gmail.com");
//        user4.put("password", "bousboss9991#");
//        user4.put("birthDate", "1999/01/29");
//        user4.put("firstName", "Boseong");
//        user4.put("lastName", "Heo");
//
//        Map<String, String> user5 = new HashMap<>();
//        user5.put("email", "rlacksgus@gmail.com");
//        user5.put("password", "chanslerk!!");
//        user5.put("birthDate", "1997/08/01");
//        user5.put("firstName", "Chanhyun");
//        user5.put("lastName", "Kim");
//
//        // when
//        assertDoesNotThrow(() -> userService.join(user4, passwordEncoder));
//        User findUser4 = userService.findUser(user4.get("email")).get();
//
//        assertDoesNotThrow(() -> userService.join(user5, passwordEncoder));
//        User findUser5 = userService.findUser(user5.get("email")).get();
//
//        assertDoesNotThrow(() -> userService.join(user1, passwordEncoder));
//        User findUser1 = userService.findUser(user1.get("email")).get();
//
//        assertDoesNotThrow(() -> userService.join(user2, passwordEncoder));
//        User findUser2 = userService.findUser(user2.get("email")).get();
//
//        assertDoesNotThrow(() -> userService.join(user3, passwordEncoder));
//        User findUser3 = userService.findUser(user3.get("email")).get();
//
//        Pageable pageable;
//        List<User> userList = userService.searchUser(pageable, "ou");
//
//        // then
//        System.out.println("User: " + userList.size());
//        System.out.println();
//
//        for(User user : userList){
//            System.out.println("user id: " + user.getId());
//        }
//        System.out.println();
//
//        boolean contains = userList.contains(findUser1);
//        System.out.println("Is user1 contained?: " + (contains ? "yes" : "no"));
//        contains = userList.contains(findUser2);
//        System.out.println("Is user2 contained?: " + (contains ? "yes" : "no"));
//        contains = userList.contains(findUser3);
//        System.out.println("Is user3 contained?: " + (contains ? "yes" : "no"));
//        contains = userList.contains(findUser4);
//        System.out.println("Is user4 contained?: " + (contains ? "yes" : "no"));
//        contains = userList.contains(findUser5);
//        System.out.println("Is user5 contained?: " + (contains ? "yes" : "no"));
//    }
}
