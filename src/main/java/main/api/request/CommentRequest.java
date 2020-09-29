package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRequest
{
    @JsonProperty("parent_id")
    private int parentId;

    @JsonProperty("post_id")
    private int postId;
    private String text;

    public CommentRequest commentRequest(int parentId, int postId, String text)
    {
        this.parentId = parentId;
        this.postId = postId;
        this.text = text;
        return this;
    }

    public int getParentId()
    {
        return parentId;
    }

    public int getPostId()
    {
        return postId;
    }

    public String getText()
    {
        return text;
    }
}