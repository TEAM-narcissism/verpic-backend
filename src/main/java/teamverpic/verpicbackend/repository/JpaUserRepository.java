package teamverpic.verpicbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import teamverpic.verpicbackend.domain.User;

import javax.transaction.Transactional;
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
