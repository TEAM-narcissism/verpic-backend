package teamverpic.verpicbackend.repository;

import teamverpic.verpicbackend.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAllByFirstName(String firstName);
    List<User> findAllByLastName(String lastName);
    Optional<User> findByEmail(String email);
    List<User> findAll();
}