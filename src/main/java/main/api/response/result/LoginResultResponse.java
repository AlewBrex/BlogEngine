package main.api.response.result;

import main.api.response.user.AllUserInformation;

public class LoginResultResponse extends OkResultResponse {
    private AllUserInformation user;

    public LoginResultResponse(boolean result) {
        super(result);
    }

    public LoginResultResponse(boolean result, AllUserInformation user) {
        super(result);
        this.user = user;
    }

    public AllUserInformation getUser() {
        return user;
    }

    public void setUser(AllUserInformation user) {
        this.user = user;
    }

    @Override
    public boolean isResult() {
        return super.isResult();
    }
}