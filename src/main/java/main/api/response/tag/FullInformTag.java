package main.api.response.tag;

import lombok.Data;
import java.util.Set;

@Data
public class FullInformTag
{
    private Set<TagResponse> tags;

    public FullInformTag(Set<TagResponse> tags)
    {
        this.tags = tags;
    }
}