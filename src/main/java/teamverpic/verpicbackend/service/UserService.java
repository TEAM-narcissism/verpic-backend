package teamverpic.verpicbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.domain.User;
import teamverpic.verpicbackend.dto.UserCRUDDto;
import teamverpic.verpicbackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public Long join(Map<String, String> user , PasswordEncoder passwordEncoder) throws ParseException, IllegalStateException{
        Date birthDate = new SimpleDateFormat("yyyy/MM/dd").parse(user.get("birthDate"));
        System.out.println("birthDate = " + birthDate);
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

    private void validateDuplicateUser(String email) throws IllegalStateException {
        userRepository.findByEmail(email)
                .ifPresent((m -> {
                    throw new IllegalStateException("이미 가입된 이메일입니다.");
                }));
    }

    public String login(Map<String, String> user, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) throws IllegalArgumentException {
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

    /**
     * 유저 검색
     * 검색 창에 입력한 string으로 firstName, lastName, email을 순서대로 조회하여 결과를 출력함
     * @param searchString
     * @return List<User>
     */
    public List<User> searchUser(String searchString){
        List<User> result=new ArrayList<>();
        HashSet<User> totalset=new HashSet<>();

        result.addAll(userRepository.findAllByFirstNameContaining(searchString));
        result=userDuplicateDelete(result, totalset);

        result.addAll(userRepository.findAllByLastNameContaining(searchString));
        result=userDuplicateDelete(result, totalset);

        result.addAll(userRepository.findAllByEmailContaining(searchString));
        result=userDuplicateDelete(result, totalset);

        return result;
    }

    // 유저 리스트 중 중복된 유저를 제거함
    private List<User> userDuplicateDelete(List<User> total, HashSet<User> totalset) {
        totalset.addAll(total);
        total.clear();
        total.addAll(totalset);
        totalset.clear();

        return total;
    }
}
