package main.api.request.change;

import lombok.Data;

@Data
public class СhangeTwoString
{
    private String name;
    private String email;

    public СhangeTwoString(String name, String email)
    {
        this.name = name;
        this.email = email;
    }
}