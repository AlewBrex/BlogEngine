package main.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUserInformationResponse {
  private Integer id;
  private String name;
  private String photo;
  private String email;
  private Boolean moderation;
  private Integer moderationCount;
  private Boolean settings;
}
