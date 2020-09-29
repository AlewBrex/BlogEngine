package main.api.response.tag;

import java.util.HashSet;
import java.util.Set;

public class FullInformTag
{
    private Set<TagResponse> nameTagResponse;

    public FullInformTag(Set<TagResponse> nameTagResponse)
    {
        this.nameTagResponse = nameTagResponse;
    }

    public Set<String> getSetNameTagResponse()
    {
        Set<String> strings = new HashSet<>();
        for (TagResponse name : nameTagResponse)
        {
            strings.add(name.getName());
        }
        return strings;
    }

    public Set<TagResponse> getFullInformTag()
    {
        return nameTagResponse;
    }
}