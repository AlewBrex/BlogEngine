package main.api.response.tag;

import lombok.Data;

@Data
public class TagResponse
{
    private String name;
    private byte weight;

    public TagResponse(String name, byte weight)
    {
        this.name = name;
        this.weight = weight;
    }
}