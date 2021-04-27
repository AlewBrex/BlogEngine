package main.api.response.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.api.response.ResultResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OkResultResponse implements ResultResponse {
  private final Boolean result = true;
}
