package teamverpic.verpicbackend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teamverpic.verpicbackend.config.security.JwtTokenProvider;
import teamverpic.verpicbackend.config.security.dto.SessionUser;
import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
import teamverpic.verpicbackend.domain.user.domain.User;
import teamverpic.verpicbackend.domain.user.dto.UserJoinDto;
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
        Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(user.get("birthDate"));
        System.out.println("birthDate = " + birthDate);
        validateDuplicateUser(user.get("email"));
        return userRepository.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .firstName(user.get("firstName"))
                .lastName(user.get("lastName"))
                .birthDate(birthDate)
                .firstLanguage(user.get("motherTongue"))
                .learnLanguage(user.get("studyLanguage"))
                .roles(Collections.singletonList("ROLE_USER")) // ?????? ????????? USER ??? ??????
                .build()).getId();
    }

    public String oauth_join(UserJoinDto userJoinDto, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) throws ParseException {

        Optional<User> user = userRepository.findByEmail(userJoinDto.getEmail());


        if(user.isPresent()){
            return jwtTokenProvider.createToken(user.get().getUsername(), user.get().getRoles());
        } else {
            User createdUser = User.builder()
                    .email(userJoinDto.getEmail())
                    .password(passwordEncoder.encode(userJoinDto.getPassword()))
                    .firstName(userJoinDto.getFirstName())
                    .lastName(userJoinDto.getLastName())
                    .firstLanguage("KOR")
                    .learnLanguage("ENG")
                    .roles(Collections.singletonList("ROLE_USER")).build();

            userRepository.save(createdUser);
            return jwtTokenProvider.createToken(createdUser.getUsername(), createdUser.getRoles());
        }
    }

    private void validateDuplicateUser(String email) throws IllegalStateException {
        userRepository.findByEmail(email)
                .ifPresent((m -> {
                    throw new IllegalStateException("?????? ????????? ??????????????????.");
                }));
    }

    public String login(Map<String, String> user, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) throws IllegalArgumentException {
        User member = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("???????????? ?????? E-MAIL ?????????."));

        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("????????? ?????????????????????.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
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
                .orElseThrow(() -> new IllegalArgumentException("?????? ???????????? ????????????. email=" + email));

        return new UserResponseDto(member);
    }

    //?????? ????????? ?????? ???
    public UserResponseDto findById(Long id) {
        User member = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("?????? ???????????? ????????????. id=" + id));

        return new UserResponseDto(member);
    }

    @Transactional
    public Long profile_update(Long id, UserUpdateRequestDto requestDto) {
        User member = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("?????? ???????????? ????????????. id=" + id));

        member.update(requestDto.getFirstName(), requestDto.getLastName(), requestDto.getBirthDate(),
                requestDto.getFirstLanguage(), requestDto.getLearnLanguage());

        return id;
    }

    // ?????? ????????? ??? ????????? ????????? ?????????
    private List<User> userDuplicateDelete(List<User> total, HashSet<User> totalset) {
        totalset.addAll(total);
        total.clear();
        total.addAll(totalset);
        totalset.clear();

        return total;
    }
}
