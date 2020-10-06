package main.api.request;

import lombok.Data;
import main.model.Tag;
import java.util.Set;

@Data
public class PostRequest
{
    private long timestamp;
    private int active;
    private String title;
    private Set<Tag> tags;
    private String text;

    public PostRequest(long timestamp, int active,
                           String title, Set<Tag> tags,
                           String text)
    {
        this.timestamp = timestamp;
        this.active = active;
        this.title = title;
        this.tags = tags;
        this.text = text;
    }
}