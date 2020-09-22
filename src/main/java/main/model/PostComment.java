package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "post_comments")
public class PostComment
{
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "parent_id")
    private PostComment parentId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, columnDefinition = "INT")
    private Post postId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT")
    private User userId;

    @Column(name = "time", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime time;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<PostComment> comments;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public PostComment getParentId()
    {
        return parentId;
    }

    public void setParentId(PostComment parentId)
    {
        this.parentId = parentId;
    }

    public Post getPostId()
    {
        return postId;
    }

    public void setPostId(Post postId)
    {
        this.postId = postId;
    }

    public User getUserId()
    {
        return userId;
    }

    public void setUserId(User userId)
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

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}