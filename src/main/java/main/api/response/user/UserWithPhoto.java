package main.api.response.user;

import lombok.Data;

@Data
public class UserWithPhoto
{
    private int id;
    private String name;
    private String photo;

    public UserWithPhoto(int id, String name, String photo)
    {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }
}