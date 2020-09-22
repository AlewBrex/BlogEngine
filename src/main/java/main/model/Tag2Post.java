package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tag2post")
public class Tag2Post
{
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "post_id", nullable = false, columnDefinition = "INT")
    private Post postId;

    @Column(name = "tag_id", nullable = false, columnDefinition = "INT")
    private Tag tagId;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Post getPost()
    {
        return postId;
    }

    public void setPost(Post postId)
    {
        this.postId = postId;
    }

    public Tag getTag()
    {
        return tagId;
    }

    public void setTag(Tag tagId)
    {
        this.tagId = tagId;
    }
}