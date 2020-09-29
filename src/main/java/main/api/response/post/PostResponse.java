package main.api.response.post;

import main.api.response.user.UserResponse;

public class PostResponse
{
    private int id;
    private long timestamp;
    private UserResponse user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

    public PostResponse infoPost(int id, long timestamp,
                                 UserResponse user1, String title,
                                 String announce, int likeCount,
                                 int dislikeCount, int commentCount,
                                 int viewCount)
    {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user1.userTwoResponse(
                user1.getId(),
                user1.getName());
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        return this;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getAnnounce() {
        return announce;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public UserResponse getUser() {
        return user;
    }
}