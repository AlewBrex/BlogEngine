package main.api.response.user;

public class UserResponse
{
    private int id;
    private String name;
    private String photo;

    public UserResponse userTwoResponse(int id, String name)
    {
        this.id = id;
        this.name = name;
        return this;
    }

    public UserResponse userTreeResponse(int id, String name, String photo)
    {
        this.id = id;
        this.name = name;
        this.photo = photo;
        return this;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getPhoto()
    {
        return photo;
    }
}