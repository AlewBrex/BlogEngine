package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest
{
    @JsonProperty("e_mail")
    private String email;
    private String password;

    public LoginRequest(String email, String password)
    {
        this.email = email;
        this.password = password;
    }
}