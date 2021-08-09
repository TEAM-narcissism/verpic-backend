package teamverpic.verpicbackend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.config.security.dto.SessionUser;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.dto.UserResponseDto;
import teamverpic.verpicbackend.domain.user.dto.UserSearchDto;
import teamverpic.verpicbackend.domain.user.dto.UserUpdateRequestDto;
import teamverpic.verpicbackend.domain.user.dao.UserRepository;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public String OAuth2_login(SessionUser user, JwtTokenProvider jwtTokenProvider) throws IllegalArgumentException {
        User member = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }

    public Optional<User> findUser(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<UserSearchDto> searchUser(Pageable pageable, String searchString){
        List<User> result=new ArrayList<>();
        HashSet<User> totalset=new HashSet<>();
        result.addAll(userRepository.findAllByFirstNameContaining(searchString));
        result=userDuplicateDelete(result, totalset);

        result.addAll(userRepository.findAllByLastNameContaining(searchString));
        result=userDuplicateDelete(result, totalset);

        result.addAll(userRepository.findAllByEmailContaining(searchString));
        result=userDuplicateDelete(result, totalset);

        int count=result.size();

        List<UserSearchDto> outcome=new ArrayList<>();
        result.forEach(user->outcome.add(new UserSearchDto(user)));

        return new PageImpl<UserSearchDto>(outcome, pageable, count);
    }

    public UserResponseDto findByEmail(String email) {
        User member = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. email=" + email));

        return new UserResponseDto(member);
    }

    //유저 프로필 조회 시
    public UserResponseDto findById(Long id) {
        User member = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        return new UserResponseDto(member);
    }

    @Transactional
    public Long profile_update(Long id, UserUpdateRequestDto requestDto) {
        User member = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        member.update(requestDto.getFirstLanguage(), requestDto.getLearnLanguage());

        return id;
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
