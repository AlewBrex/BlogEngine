package main.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CaptchaResponse {
  private String secret;
  private String image;
}
