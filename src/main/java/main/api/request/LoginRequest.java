package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest
{
    @JsonProperty("e_mail")
    private String email;
    private String password;

    public LoginRequest loginRequest(String email, String password)
    {
        this.email = email;
        this.password = password;
        return this;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }
}