package main.api.response.post;

import lombok.Data;
import java.util.Set;

@Data
public class CountPostResponse
{
    private int count;
    private Set<PostResponse> posts;

    public CountPostResponse(int count, Set<PostResponse> posts)
    {
        this.count = count;
        this.posts = posts;
    }
}
