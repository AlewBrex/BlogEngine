package main.api.response.result;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BadResultResponse
{
    @JsonProperty("result")
    private OkResultResponse okResultResponse;
    private Map<String, String> errors;

    public BadResultResponse badResultResponse(OkResultResponse okResultResponse1,
                                               Map<String, String> errors)
    {
        this.okResultResponse = okResultResponse1.okResultResponse(okResultResponse1.isResult());
        this.errors = errors;
        return this;
    }

    public OkResultResponse getOkResultResponse()
    {
        return okResultResponse;
    }

    public Map<String, String> getErrors()
    {
        return errors;
    }
}