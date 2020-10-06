package main.api.response.result;

import lombok.Data;
import java.util.Map;

@Data
public class BadResultResponse
{
    private OkResultResponse result;
    private Map<String, String> errors;

    public BadResultResponse(OkResultResponse result,
                             Map<String, String> errors)
    {
        this.result = result;
        this.errors = errors;
    }
}