package teamverpic.verpicbackend.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByFirstName(String firstName);

    List<User> findAllByLastName(String lastName);

    // 유저 검색을 위한 함수
    List<User> findAllByFirstNameContaining(String partOfFirstName);

    List<User> findAllByLastNameContaining(String partOfLastName);

    List<User> findAllByEmailContaining(String partOfEmail);
}
