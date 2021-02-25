package main.api.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.api.response.ResultResponse;

import java.util.List;

@Data
@AllArgsConstructor
public class CountPostResponse implements ResultResponse {
    private int count;
    private List<PostResponse> posts;
}