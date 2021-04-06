package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class CommentRequest {
  @JsonProperty("parent_id")
  private int parentId;

  @JsonProperty("post_id")
  private int postId;

  private String text;
  private MultipartFile photo;

  public CommentRequest(int parentId, int postId, String text) {
    this.parentId = parentId;
    this.postId = postId;
    this.text = text;
  }

  public CommentRequest(int parentId, int postId, String text, MultipartFile photo) {
    this.parentId = parentId;
    this.postId = postId;
    this.text = text;
    this.photo = photo;
  }
}
