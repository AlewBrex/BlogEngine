package main.api.request.change;

import lombok.Data;

@Data
public class ChangeTreeString {
    private String name;
    private String email;
    private String password;

    public ChangeTreeString(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public ChangeTreeString(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}