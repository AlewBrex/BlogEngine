package main.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUserInformationResponse {
  private int id;
  private String name;
  private String photo;
  private String email;
  private boolean moderation;
  private int moderationCount;
  private boolean settings;
}
