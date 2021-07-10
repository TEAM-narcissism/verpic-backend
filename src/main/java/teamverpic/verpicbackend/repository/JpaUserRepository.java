package teamverpic.verpicbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamverpic.verpicbackend.domain.User;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

    @Override
    Optional<User> findByEmail(String email);

    @Override
    List<User> findAllByFirstName(String firstName);

    @Override
    List<User> findAllByLastName(String lastName);
}
