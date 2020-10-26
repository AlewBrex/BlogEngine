package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.CommentResponse;
import main.api.response.user.UserResponse;
import java.util.Set;

@Data
@AllArgsConstructor
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
}