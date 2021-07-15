package teamverpic.verpicbackend.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import teamverpic.verpicbackend.domain.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
}
