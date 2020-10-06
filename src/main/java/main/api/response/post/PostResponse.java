package main.api.response.post;

import lombok.Data;
import main.api.response.user.UserResponse;

@Data
public class PostResponse
{
    private int id;
    private long timestamp;
    private UserResponse user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

    public PostResponse(int id, long timestamp,
                            UserResponse user, String title,
                            String announce, int likeCount,
                            int dislikeCount, int commentCount,
                            int viewCount)
    {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
    }
}