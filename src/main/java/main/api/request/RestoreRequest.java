package main.api.request;

public class RestoreRequest
{
    private String email;

    public RestoreRequest restoreRequest(String email)
    {
        this.email = email;
        return this;
    }

    public String getEmail()
    {
        return email;
    }
}