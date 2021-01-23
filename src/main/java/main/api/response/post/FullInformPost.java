package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.CommentResponse;
import main.api.response.user.UserResponse;
import main.api.response.user.UserWithPhoto;
import main.model.Comment;
import main.model.Post;
import main.model.Tag;

import java.time.ZoneId;
import java.util.Set;

@Data
@AllArgsConstructor
public class FullInformPost {
    private int id;
    private long timestamp;
    private boolean active;
    private UserResponse user;
    private String title;
    private String text;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
    private Set<CommentResponse> comments;
    private Set<String> tags;

    public FullInformPost(Post post) {
        id = post.getId();
        timestamp = post.getTime().
                atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        active = itTrue(post.getIsActive());
        user = new UserResponse(post.getUsers().getId(), post.getUsers().getName());
        title = post.getTitle();
        text = post.getText();
        likeCount = (int) post.getVotes().stream().filter(l -> l.getValue() == 1).count();
        dislikeCount = (int) post.getVotes().stream().filter(d -> d.getValue() == -1).count();
        commentCount = post.getComments().size();
        viewCount = post.getViewCount();
        for (Comment c : post.getComments()) {
            CommentResponse commentResponse = new CommentResponse(c.getId(), c.getTime().
                    atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), c.getText(),
                    new UserWithPhoto(c.getUsers().getId(), c.getUsers().getName(), c.getUsers().getPhoto()));
            comments.add(commentResponse);
        }
        for (Tag t : post.getTags()) {
            String tagPost = t.getName();
            tags.add(tagPost);
        }
    }

    private boolean itTrue(int active) {
        return active == 1;
    }
}