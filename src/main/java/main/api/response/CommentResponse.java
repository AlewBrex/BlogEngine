package main.api.response;

import main.api.response.user.UserResponse;

public class CommentResponse
{
    private int id;
    private long timestamp;
    private String text;
    private UserResponse userResponse;


    public CommentResponse commentResponse(int id, long timestamp, String text, UserResponse userResponse1)
    {
        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.userResponse = userResponse1.userTreeResponse(
                userResponse1.getId(),
                userResponse1.getName(),
                userResponse1.getPhoto());
        return this;
    }

    public int getId()
    {
        return id;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public String getText()
    {
        return text;
    }

    public UserResponse getUserResponse()
    {
        return userResponse;
    }
}