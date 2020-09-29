package main.api.response.tag;

public class TagResponse
{
    private String name;
    private byte weight;

    public TagResponse tagResponse(String name, byte weight)
    {
        this.name = name;
        this.weight = weight;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public byte getWeight()
    {
        return weight;
    }
}