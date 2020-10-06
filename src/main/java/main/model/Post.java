package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
public class Post
{
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT")
    private int isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", nullable = false, columnDefinition = "enum default 'NEW'")
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id", columnDefinition = "INT")
    private User moderatorId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT")
    private User userId;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime time;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "view_count", nullable = false, columnDefinition = "INT")
    private int viewCount;

    @ManyToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @JoinTable(name = "posts_tags", joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<PostComment> postComments;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<PostVote> postVotes;

    public Post(int id, int isActive, ModerationStatus moderationStatus, User moderatorId, User userId,
                LocalDateTime time, String title, String text, int viewCount)
    {
        this.id = id;
        this.isActive = isActive;
        this.moderationStatus = moderationStatus;
        this.moderatorId = moderatorId;
        this.userId = userId;
        this.time = time;
        this.title = title;
        this.text = text;
        this.viewCount = viewCount;
    }

    public enum  ModerationStatus
    {
        NEW,
        ACCEPTED,
        DECLINED
    }
}