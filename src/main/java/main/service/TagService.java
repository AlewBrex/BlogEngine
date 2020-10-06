package main.service;

import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TagService
{
    @Autowired
    private TagRepository tagRepository;
}