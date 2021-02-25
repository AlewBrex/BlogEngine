package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @JsonProperty("e_mail")
    private String email;
    private String password;
    private String captcha;
    private String name;
    @JsonProperty("captcha_secret")
    private String captchaSecret;
}