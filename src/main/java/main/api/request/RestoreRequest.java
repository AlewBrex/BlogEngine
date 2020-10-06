package main.api.request;

import lombok.Data;

@Data
public class RestoreRequest
{
    private String email;

    public RestoreRequest(String email)
    {
        this.email = email;
    }
}