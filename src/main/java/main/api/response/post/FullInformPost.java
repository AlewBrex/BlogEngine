package main.api.response.post;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.CommentResponse;
import main.api.response.ResultResponse;
import main.api.response.user.UserResponse;

import java.util.List;

@Data
@RequiredArgsConstructor
public class FullInformPost implements ResultResponse {
  private Integer id;
  private Long timestamp;
  private Boolean active;
  private UserResponse user;
  private String title;
  private String text;
  private String announce;
  private Integer likeCount;
  private Integer dislikeCount;
  private Integer commentCount;
  private Integer viewCount;
  private List<CommentResponse> comments;
  private List<String> tags;
}
