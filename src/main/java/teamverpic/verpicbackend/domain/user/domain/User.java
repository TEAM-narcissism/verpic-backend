package teamverpic.verpicbackend.domain.user.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import teamverpic.verpicbackend.domain.analysis.domain.AudioFile;
import teamverpic.verpicbackend.domain.preview.domain.UserAnswer;
import teamverpic.verpicbackend.domain.reservation.domain.StudyReservation;
import teamverpic.verpicbackend.domain.matching.domain.MatchUser;

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

    private String picture;

    private String firstLanguage;

    private String learnLanguage;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserAnswer> userAnswerList;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AudioFile> audioFileList = new ArrayList<>();

    public void addUserAnswer(UserAnswer userAnswer) {
        userAnswer.setUser(this);
        this.userAnswerList.add(userAnswer);
    }



    public void addAudioFile(AudioFile audioFile) {
        audioFile.setUser(this);
        this.audioFileList.add(audioFile);
    }

    public String getRoleKey() {
        return this.roles.get(0);
    }

    // OAuth2 Login
    public User OAuth_update(String name, String picture) {
        this.firstName = name;
        this.picture = picture;

        return this;
    }

    public void update(String firstName, String lastName, Date birthDate,
                       String firstLanguage, String learnLanguage) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.birthDate=birthDate;
        this.firstLanguage = firstLanguage;
        this.learnLanguage = learnLanguage;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name= "relationship",
            joinColumns = @JoinColumn(name="user1"),
            inverseJoinColumns = @JoinColumn(name="user2")
    )
    @Builder.Default
    private Set<User> userRelation = new HashSet<User>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<MatchUser> matches = Collections.synchronizedList(new ArrayList<>());

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

    public void addUserMatch(MatchUser matchUser) {
        matches.add(matchUser);
    }

    public void deleteUserMatch(MatchUser matchUser) {
        matches.remove(matchUser);
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
