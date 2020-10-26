package main.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithPhoto
{
    private int id;
    private String name;
    private String photo;
}