package main.service.interfaces;

import main.model.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    User getCurrentUserByEmail(String email);
}
