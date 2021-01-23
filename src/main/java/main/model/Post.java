package main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.criterion.Example;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
public class Post {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT")
    private int isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", nullable = false, columnDefinition = "enum default 'NEW'")
    private ModerationStatus moderationStatus;

    @JoinColumn(name = "moderator_id", columnDefinition = "INT")
    private Integer isModerator;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT")
    private User users;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime time;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "view_count", nullable = false, columnDefinition = "INT")
    private int viewCount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tag2posts", joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Vote> votes;

    public Post(int isActive, User users, LocalDateTime time,
                String title, String text, Set<Tag> tags) {
        this.isActive = isActive;
        this.users = users;
        this.time = time;
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    public enum ModerationStatus {
        NEW,
        ACCEPTED,
        DECLINED
    }
}