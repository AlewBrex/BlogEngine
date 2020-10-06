package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequest
{
    @JsonProperty("e_mail")
    private String email;
    private String password;
    private String captcha;
    private String name;
    @JsonProperty("captcha_secret")
    private String captchaSecret;

    public RegisterRequest(String email, String password,
                           String captcha, String name,
                           String captchaSecret)
    {
        this.email = email;
        this.password = password;
        this.captcha = captcha;
        this.name = name;
        this.captchaSecret = captchaSecret;
    }
}