package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {
  @JsonProperty("parent_id")
  private Integer parentId;

  @JsonProperty("post_id")
  private Integer postId;

  private String text;

  public CommentRequest(Integer parentId, Integer postId, String text) {
    this.parentId = parentId;
    this.postId = postId;
    this.text = text;
  }

  public CommentRequest(Integer postId, String text) {
    this.postId = postId;
    this.text = text;
  }
}
