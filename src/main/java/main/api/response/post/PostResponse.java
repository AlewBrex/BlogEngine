package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import main.api.response.ResultResponse;
import main.api.response.user.UserResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse implements ResultResponse {
  private Integer id;
  private Long timestamp;
  private UserResponse user;
  private String title;
  private String announce;
  private Integer likeCount;
  private Integer dislikeCount;
  private Integer commentCount;
  private Integer viewCount;
}
