package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ModerationRequest {
  @JsonProperty("post_id")
  private Integer postId;

  private String decision;
}
