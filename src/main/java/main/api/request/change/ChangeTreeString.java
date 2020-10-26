package main.api.request.change;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeTreeString
{
    private String name;
    private String email;
    private String password;
}