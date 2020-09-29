package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT")
    private User userId;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime time;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false, columnDefinition = "INT")
    private int viewCount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "posts_tags", joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<PostComment> postComments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<PostVote> postVotes;

    public Post(@NotNull int id, int isActive, ModerationStatus moderationStatus, User moderatorId, User userId,
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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive)
    {
        this.isActive = isActive;
    }

    public ModerationStatus getModerationStatus()
    {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus)
    {
        this.moderationStatus = moderationStatus;
    }

    public User getModeratorId()
    {
        return moderatorId;
    }

    public void setModeratorId(User moderatorId)
    {
        this.moderatorId = moderatorId;
    }

    public User getUser()
    {
        return userId;
    }

    public void setUser(User userId)
    {
        this.userId = userId;
    }

    public LocalDateTime getTime()
    {
        return time;
    }

    public void setTime(LocalDateTime time)
    {
        this.time = time;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public int getViewCount()
    {
        return viewCount;
    }

    public void setViewCount(int viewCount)
    {
        this.viewCount = viewCount;
    }

    public enum  ModerationStatus
    {
        NEW,
        ACCEPTED,
        DECLINED
    }
}