package main.api.response.post;

import lombok.Data;
import main.api.response.CommentResponse;
import main.api.response.user.UserResponse;
import java.util.Set;

@Data
public class FullInformPost
{
    private int id;
    private long timestamp;
    private boolean active;
    private UserResponse user;
    private String title;
    private String text;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
    private Set<CommentResponse> comments;
    private Set<String> tags;

    public FullInformPost(int id, long timestamp, boolean active,
                          UserResponse user, String title, String text,
                          String announce, int likeCount, int dislikeCount,
                          int commentCount, int viewCount,
                          Set<CommentResponse> comments, Set<String> tags)
    {
        this.id = id;
        this.timestamp = timestamp;
        this.active = active;
        this.user = user;
        this.title = title;
        this.text = text;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.comments = comments;
        this.tags = tags;
    }
}