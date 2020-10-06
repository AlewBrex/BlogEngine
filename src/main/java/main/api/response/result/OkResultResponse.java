package main.api.response.result;

import lombok.Data;

@Data
public class OkResultResponse
{
    private boolean result;

    public OkResultResponse(boolean result)
    {
        this.result = result;
    }
}