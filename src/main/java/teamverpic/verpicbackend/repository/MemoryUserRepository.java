package teamverpic.verpicbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamverpic.verpicbackend.domain.User;

import java.util.*;
import java.util.stream.Collectors;

public class MemoryUserRepository implements UserRepository{

    private static Map<Long, User> store = new HashMap<>();

    @Override
    public User save(User user) {
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<User> findAllByFirstName(String firstName) {
        return store.values().stream()
                .filter(user -> user.getFirstName().equals(firstName)).collect(Collectors.toList());
    }

    @Override
    public List<User> findAllByLastName(String lastName) {
        return store.values().stream()
                .filter(user -> user.getFirstName().equals(lastName)).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    public void clearStore() {
        store.clear();
    }

    // 유저 검색
    @Override
    public List<User> findAllByFirstNameContaining(String partOfFirstName){

        return store.values().stream()
                .filter(user -> user.getFirstName().contains(partOfFirstName))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllByLastNameContaining(String partOfLastName){
        return store.values().stream()
                .filter(user -> user.getLastName().contains(partOfLastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllByEmailContaining(String partOfEmail){
        return store.values().stream()
                .filter(user -> user.getEmail().contains(partOfEmail))
                .collect(Collectors.toList());
    }
}
