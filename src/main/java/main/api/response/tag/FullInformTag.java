package main.api.response.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class FullInformTag
{
    private Set<TagResponse> tags;
}