package main.api.response.result;

import lombok.Data;
import main.api.response.user.AllUserInformation;

@Data
public class LoginResultResponse
{
    private OkResultResponse result;
    private AllUserInformation user;

    public LoginResultResponse(OkResultResponse result,
                               AllUserInformation user)
    {
        this.result = result;
        this.user = user;
    }
}