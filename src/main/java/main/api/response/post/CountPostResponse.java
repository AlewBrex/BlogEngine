package main.api.response.post;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.ResultResponse;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CountPostResponse implements ResultResponse {
  private Integer count;
  private List<PostResponse> posts;
}
