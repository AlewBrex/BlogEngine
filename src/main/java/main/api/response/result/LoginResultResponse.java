package main.api.response.result;

import lombok.Data;
import main.api.response.ResultResponse;
import main.api.response.user.AllUserInformationResponse;

@Data
public class LoginResultResponse implements ResultResponse {
    private boolean result;
    private AllUserInformationResponse user;

    public LoginResultResponse(AllUserInformationResponse user) {
        this.result = true;
        this.user = user;
    }

    public AllUserInformationResponse getUser() {
        return user;
    }

    public void setUser(AllUserInformationResponse user) {
        this.user = user;
    }
}