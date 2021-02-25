package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.ResultResponse;
import main.api.response.user.UserResponse;

@Data
@AllArgsConstructor
public class PostResponse implements ResultResponse {
    private int id;
    private long timestamp;
    private UserResponse user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}