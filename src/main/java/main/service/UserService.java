package main.service;

import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService
{
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

}