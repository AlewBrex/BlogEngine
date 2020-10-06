package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LikeDislikeRequest
{
    @JsonProperty("post_id")
    private int postId;

    public LikeDislikeRequest (int postId)
    {
        this.postId = postId;
    }
}