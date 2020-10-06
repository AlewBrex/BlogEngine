package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
public class Tag
{
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @ManyToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Tag2Post> tag2Posts;
}