package main.api.response.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserWithPhotoResponse {
  private Integer id;
  private String name;
  private String photo;
}
