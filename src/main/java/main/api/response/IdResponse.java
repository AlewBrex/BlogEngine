package main.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class IdResponse implements ResultResponse {
  private Integer id;
}
