package teamverpic.verpicbackend.domain.user.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private Date birthDate;

    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 300, nullable = false)
    private String password;

    @Column
    private String picture;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    // OAuth2 Login
    public User OAuth_update(String name, String picture) {
        this.firstName = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.roles.get(0);
    }


    // 프로필 설정 시 추가 (detail_profile)
    private String nation;
    private String gender;
    private String firstLanguage;
    private String availableLanguage;
    private String learnLanguage;
    private String hobby;
    private String interest;

    public void update(String firstLanguage, String availableLanguage, String learnLanguage, String hobby, String interest) {
        this.firstLanguage = firstLanguage;
        this.availableLanguage = availableLanguage;
        this.learnLanguage = learnLanguage;
        this.hobby = hobby;
        this.interest = interest;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name= "relationship",
            joinColumns = @JoinColumn(name="user1"),
            inverseJoinColumns = @JoinColumn(name="user2")
    )
    @Builder.Default
    private Set<User> userRelation = new HashSet<User>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void setUserRelation(User requestedUser){
        userRelation.add(requestedUser);
    }

    public void deleteUserRelation(User deleteUser) {
        userRelation.remove(deleteUser);
    }



    @Override
    public String getUsername() {
        return email; // User Identification ? : email
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
