package main.api.response.result;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.ResultResponse;

@Data
@RequiredArgsConstructor
public class OkResultResponse implements ResultResponse {
  private final Boolean result = true;
}
