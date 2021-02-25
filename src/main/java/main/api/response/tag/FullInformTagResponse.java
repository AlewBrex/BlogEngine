package main.api.response.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FullInformTagResponse {
    private List<TagResponse> tags;
}