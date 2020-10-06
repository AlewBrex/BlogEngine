package main.service;

import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService
{
    @Autowired
    private UserRepository userRepository;
}