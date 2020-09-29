package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordRequest
{
    private String code;
    private String password;
    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;

    public ChangePasswordRequest changePasswordRequest(String code, String password,
                                 String captcha, String captchaSecret)
    {
        this.code = code;
        this.password = password;
        this.captcha = captcha;
        this.captchaSecret = captchaSecret;
        return this;
    }

    public String getCode()
    {
        return code;
    }

    public String getPassword()
    {
        return password;
    }

    public String getCaptcha()
    {
        return captcha;
    }

    public String getCaptchaSecret()
    {
        return captchaSecret;
    }
}