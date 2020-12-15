package main.api.response.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FullInformTag
{
    private List<TagResponse> tags;
}