package main.api.request.change;

import lombok.Data;

@Data
public class ChangeProfileRequest
{
    private String name;
    private String email;
    private String password;
    private byte removePhoto;

    public ChangeProfileRequest(String name, String email,
                                String password, byte removePhoto)
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.removePhoto = removePhoto;
    }
}