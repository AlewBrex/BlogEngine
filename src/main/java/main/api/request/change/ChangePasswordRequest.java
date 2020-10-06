package main.api.request.change;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangePasswordRequest
{
    private String code;
    private String password;
    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;

    public ChangePasswordRequest(String code, String password,
                                 String captcha, String captchaSecret)
    {
        this.code = code;
        this.password = password;
        this.captcha = captcha;
        this.captchaSecret = captchaSecret;
    }
}