package main.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.user.UserWithPhotoResponse;

@Data
@RequiredArgsConstructor
public class CommentResponse {
  private Integer id;
  private Long timestamp;
  private String text;
  private UserWithPhotoResponse user;
}
