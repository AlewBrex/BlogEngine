package main.service;

import lombok.extern.log4j.Log4j2;
import main.api.response.post.CountPostResponse;
import main.api.response.post.PostResponse;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class PostService
{
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository)
    {
        this.postRepository = postRepository;
    }

    public CountPostResponse getPosts(String mode, int offset, int limit)
    {
        List<PostResponse> list = new ArrayList<>();
        int count = postRepository.countPostForMod();

        switch (mode)
        {
            case ("recent"):
                list.addAll(postRepository.modeRecent(offset,limit));
                break;
            case ("popular"):
                list.addAll(postRepository.modePopular(offset,limit));
                break;
            case ("best"):
                list.addAll(postRepository.modeBest(offset, limit));
                break;
            case ("early"):
                list.addAll(postRepository.modeEarl(offset, limit));
        }
        return new CountPostResponse(count, list);
    }

    public int countPostsForModeration()
    {
        return postRepository.countPostsForModeration();
    }
}