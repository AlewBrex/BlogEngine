package main.api.response.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagResponse
{
    private String name;
    private byte weight;
}