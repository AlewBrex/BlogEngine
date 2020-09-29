package main.api.response.post;

import main.api.response.CommentResponse;
import main.api.response.tag.FullInformTag;
import main.api.response.user.UserResponse;

import java.util.Set;

public class FullInformPost
{
    private FullInformTag fullInformTag;
    private boolean active;
    private UserResponse user;
    private PostResponse postResponse;
    private String text;
    private Set<CommentResponse> comments;
    private Set<String> tags;

    public FullInformPost(boolean active, UserResponse user1,
                          PostResponse postResponse, String text,
                          Set<CommentResponse> comments,
                          Set<String> tags)
    {
        this.active = active;
        this.user = user1.userTwoResponse(
                user1.getId(),
                user1.getName());
        this.postResponse = postResponse;
        this.text = text;
        this.comments = comments;
        this.tags = tags;
    }

    public boolean isActive() {
        return active;
    }

    public UserResponse getUser() {
        return user;
    }

    public PostResponse getPostResponse() {
        return postResponse;
    }

    public String getText() {
        return text;
    }

    public Set<CommentResponse> getComments() {
        return comments;
    }

    public Set<String> getTags()
    {
        return fullInformTag.getSetNameTagResponse();
    }
}