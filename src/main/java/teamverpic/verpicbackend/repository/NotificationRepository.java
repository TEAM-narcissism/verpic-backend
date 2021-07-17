package teamverpic.verpicbackend.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import teamverpic.verpicbackend.domain.Notification;
import teamverpic.verpicbackend.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    Optional<Notification> findByReceiverId(Long id);

    Optional<Notification> findBy_id(String id);

    void deleteBy_id(String id);
}
