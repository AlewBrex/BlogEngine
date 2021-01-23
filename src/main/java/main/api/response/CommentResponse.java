package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.user.UserWithPhoto;

@Data
@AllArgsConstructor
public class CommentResponse {
    private int id;
    private long timestamp;
    private String text;
    private UserWithPhoto user;
}