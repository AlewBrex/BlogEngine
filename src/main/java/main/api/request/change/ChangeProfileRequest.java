package main.api.request.change;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeProfileRequest
{
    private String name;
    private String email;
    private String password;
    private byte removePhoto;
}