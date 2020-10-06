package main.api.response;

import lombok.Data;
import main.api.response.user.UserWithPhoto;

@Data
public class CommentResponse
{
    private int id;
    private long timestamp;
    private String text;
    private UserWithPhoto user;

    public CommentResponse(int id, long timestamp, String text,
                           UserWithPhoto user)
    {
        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
    }
}