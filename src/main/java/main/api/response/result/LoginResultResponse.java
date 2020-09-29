package main.api.response.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.api.response.user.AllUserInformation;

public class LoginResultResponse
{
    @JsonProperty("result")
    private OkResultResponse okResultResponse;
    @JsonProperty("user")
    private AllUserInformation allUserInformation;

    public LoginResultResponse(OkResultResponse okResultResponse1,
                               AllUserInformation allUserInformation1)
    {
        this.okResultResponse = okResultResponse1.okResultResponse(okResultResponse1.isResult());
        this.allUserInformation = allUserInformation1;
    }

    public OkResultResponse getOkResultResponse()
    {
        return okResultResponse;
    }

    public AllUserInformation getAllUserInformation()
    {
        return allUserInformation;
    }
}