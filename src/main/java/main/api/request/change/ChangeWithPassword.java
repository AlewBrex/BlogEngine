package main.api.request.change;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeWithPassword
{
    private String name;
    private String email;
    private byte removePhoto;
    private String photo;
}