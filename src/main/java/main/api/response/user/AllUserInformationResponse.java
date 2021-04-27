package main.api.response.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AllUserInformationResponse {
  private Integer id;
  private String name;
  private String photo;
  private String email;
  private Boolean moderation;
  private Integer moderationCount;
  private Boolean settings;
}
