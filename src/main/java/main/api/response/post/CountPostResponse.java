package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class CountPostResponse
{
    private int count;
    private Set<PostResponse> posts;
}
