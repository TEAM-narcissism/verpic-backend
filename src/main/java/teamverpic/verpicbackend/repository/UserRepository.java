package teamverpic.verpicbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamverpic.verpicbackend.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAllByFirstName(String firstName);
    List<User> findAllByLastName(String lastName);
    Optional<User> findByEmail(String email);

    // 유저 검색
    List<User> findAllByFirstNameContaining(String partOfFirstName);
    List<User> findAllByLastNameContaining(String partOfLastName);
    List<User> findAllByEmailContaining(String partOfEmail);
}
