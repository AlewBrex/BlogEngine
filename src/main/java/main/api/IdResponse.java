package main.api;

import lombok.Data;

@Data
public class IdResponse
{
    private int id;

    public IdResponse(int id)
    {
        this.id = id;
    }
}