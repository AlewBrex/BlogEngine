package main.api.response.post;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.ResultResponse;
import main.api.response.user.UserResponse;

@Data
@RequiredArgsConstructor
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
