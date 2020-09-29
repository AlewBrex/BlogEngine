package main.api.response.result;

public class OkResultResponse
{
    private boolean result;

    public OkResultResponse okResultResponse(boolean result)
    {
        this.result = result;
        return this;
    }

    public boolean isResult()
    {
        return result;
    }
}