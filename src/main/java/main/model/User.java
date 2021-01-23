package main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_moderator", nullable = false, columnDefinition = "TINYINT")
    private int isModerator;

    @Column(name = "reg_time", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime regTime;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(name = "code", columnDefinition = "VARCHAR(255)")
    private String code;

    @Column(name = "photo", nullable = false, columnDefinition = "TEXT")
    private String photo;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Post> posts;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Post> moderator;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Vote> votes;

    public User(LocalDateTime regTime, String name, String email,
                String password) {
        this.regTime = regTime;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}