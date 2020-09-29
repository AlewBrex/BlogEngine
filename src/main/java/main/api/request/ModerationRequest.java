package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationRequest
{
    @JsonProperty("post_id")
    private int postId;
    private String decision;

    public ModerationRequest moderationRequest(int postId, String decision)
    {
        this.postId = postId;
        this.decision = decision;
        return this;
    }

    public int getPostId()
    {
        return postId;
    }

    public String getDecision()
    {
        return decision;
    }
}