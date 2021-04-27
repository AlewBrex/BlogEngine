package main.api.request.change;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChangePasswordRequest {
  private String code;
  private String password;
  private String captcha;

  @JsonProperty("captcha_secret")
  private String captchaSecret;
}
