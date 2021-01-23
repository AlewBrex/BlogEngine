package main.api.response.result;

import java.util.Map;

public class BadResultResponse extends OkResultResponse {
    private Map<String, String> errors;

    public BadResultResponse(boolean result) {
        super(result);
    }

    public BadResultResponse(boolean result, Map<String, String> errors) {
        super(result);
        this.errors = errors;
    }
}