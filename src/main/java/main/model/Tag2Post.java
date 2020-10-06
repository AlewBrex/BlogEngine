package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tag2post")
@Data
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
}