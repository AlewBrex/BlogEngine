package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.api.response.user.UserWithPhotoResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
  private Integer id;
  private Long timestamp;
  private String text;
  private UserWithPhotoResponse user;
}
