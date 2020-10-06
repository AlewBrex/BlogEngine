package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User
{
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

    @Column(name = "code", nullable = false, columnDefinition = "VARCHAR(255)")
    private String code;

    @Column(name = "photo", nullable = false, columnDefinition = "TEXT")
    private String photo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Post> author;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Post> moderator;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<PostComment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<PostVote> votes;
}