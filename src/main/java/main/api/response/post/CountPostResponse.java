package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CountPostResponse
{
    private int count;
    private List<PostResponse> posts;
}
