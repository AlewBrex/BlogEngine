package main.api.request;

import main.model.Tag;
import java.util.Set;

public class PostRequest
{
    private long timestamp;
    private int active;
    private String title;
    private Set<Tag> tags;
    private String text;

    public PostRequest postRequest(long timestamp, int active,
                                   String title, Set<Tag> tags,
                                   String text)
    {
        this.timestamp = timestamp;
        this.active = active;
        this.title = title;
        this.tags = tags;
        this.text = text;
        return this;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public int getActive() {
        return active;
    }

    public String getTitle()
    {

        return title;
    }

    public Set<Tag> getTags()
    {
        return tags;
    }

    public String getText()
    {
        return text;
    }
}