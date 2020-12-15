package main.api.response.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.user.AllUserInformation;

@Data
@AllArgsConstructor
public class LoginResultResponse
{
    private boolean result;
    private AllUserInformation user;
}