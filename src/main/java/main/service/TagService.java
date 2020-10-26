package main.service;

import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TagService
{
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository)
    {
        this.tagRepository = tagRepository;
    }
}