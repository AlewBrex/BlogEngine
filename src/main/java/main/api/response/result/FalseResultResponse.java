package main.api.response.result;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.ResultResponse;

@Data
@RequiredArgsConstructor
public class FalseResultResponse implements ResultResponse {
    private final boolean result = false;
}
