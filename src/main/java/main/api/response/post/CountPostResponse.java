package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.api.response.ResultResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountPostResponse implements ResultResponse {
  private Integer count;
  private List<PostResponse> posts;
}
