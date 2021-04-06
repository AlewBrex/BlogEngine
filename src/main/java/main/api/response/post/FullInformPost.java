package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.api.response.CommentResponse;
import main.api.response.ResultResponse;
import main.api.response.user.UserResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullInformPost implements ResultResponse {
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
  private List<CommentResponse> comments;
  private List<String> tags;
}
