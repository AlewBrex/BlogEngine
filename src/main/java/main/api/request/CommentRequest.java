package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentRequest
{
    @JsonProperty("parent_id")
    private int parentId;

    @JsonProperty("post_id")
    private int postId;
    private String text;

    public CommentRequest(int parentId, int postId, String text)
    {
        this.parentId = parentId;
        this.postId = postId;
        this.text = text;
    }
}