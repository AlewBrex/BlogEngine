package main.api.response.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.ResultResponse;

@Data
@AllArgsConstructor
public class OkResultResponse implements ResultResponse {
    private final boolean result = true;
}