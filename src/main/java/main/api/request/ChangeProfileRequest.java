package main.api.request;

public class ChangeProfileRequest
{
    private String name;
    private String email;
    private String password;
    private byte removePhoto;

    public ChangeProfileRequest changeNameEmailRequest(String name, String email)
    {
        this.name = name;
        this.email = email;
        return this;
    }

    public ChangeProfileRequest changeNameEmailPassRequest(String name, String email, String pass)
    {
        this.name = name;
        this.email = email;
        this.password = pass;
        return this;
    }

    public ChangeProfileRequest changeRemovePhotoRequest(String name, String email, byte remove)
    {
        this.name = name;
        this.email = email;
        this.removePhoto = remove;
        return this;
    }

    public ChangeProfileRequest changeAllProfileRequest(String name, String email,
                                                     String password, byte removePhoto)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.removePhoto = removePhoto;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public byte getRemovePhoto()
    {
        return removePhoto;
    }
}