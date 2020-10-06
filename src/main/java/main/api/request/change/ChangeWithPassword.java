package main.api.request.change;

import lombok.Data;

@Data
public class ChangeWithPassword
{
    private String name;
    private String email;
    private byte removePhoto;
    private String photo;

    public ChangeWithPassword(String name, String email, byte removePhoto, String photo)
    {
        this.name = name;
        this.email = email;
        this.removePhoto = removePhoto;
        this.photo = photo;
    }
}