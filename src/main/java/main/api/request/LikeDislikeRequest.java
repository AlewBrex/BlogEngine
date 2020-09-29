package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LikeDislikeRequest
{
    @JsonProperty("post_id")
    private int postId;

    public LikeDislikeRequest likeRequest(int postId)
    {
        this.postId = postId;
        return this;
    }

    public int getPostId()
    {
        return postId;
    }
}