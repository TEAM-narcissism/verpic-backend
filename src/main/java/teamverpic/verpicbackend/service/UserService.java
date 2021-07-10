package teamverpic.verpicbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long join(Map<String, String> user , PasswordEncoder passwordEncoder) throws ParseException {
        Date birthDate = new SimpleDateFormat("yyyy/mm/dd").parse(user.get("birthDate"));
        validateDuplicateUser(user.get("email"));
        return userRepository.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .firstName(user.get("firstName"))
                .lastName(user.get("lastName"))
                .birthDate(birthDate)
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();
    }

    private void validateDuplicateUser(String email) {
        userRepository.findByEmail(email)
                .ifPresent((m -> {
                    throw new IllegalStateException("이미 가입된 이메일입니다.");
                }));
    }

    public String login(Map<String, String> user, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        User member = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }

    public Optional<User> findUser(String email) {
        return userRepository.findByEmail(email);
    }
}
