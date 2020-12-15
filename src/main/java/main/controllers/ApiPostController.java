package main.controllers;

import lombok.extern.log4j.Log4j2;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/post/")
public class ApiPostController
{
    private final PostService postService;

    @Autowired
    public ApiPostController(PostService postService)
    {
        this.postService = postService;
    }

    @GetMapping(value = "", params = {"offset", "limit", "mode"})
    public ResponseEntity getPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "mode") String mode)
    {
        return new ResponseEntity(postService.getPosts(mode, offset, limit), HttpStatus.OK);
    }
//
//    @GetMapping(value = "search", params = {"offset", "limit", "query"})
//    public ResponseEntity searchPost(
//            @RequestParam(name = "offset") int offset,
//            @RequestParam(name = "limit") int limit,
//            @RequestParam(name = "query") String query)
//    {
//        return null;
//    }
//
//    @GetMapping(value = "byDate", params = {"offset", "limit", "date"})
//    public ResponseEntity getPostByDate(
//            @RequestParam(name = "offset") int offset,
//            @RequestParam(name = "limit") int limit,
//            @RequestParam(name = "date") String date)
//    {
//        return null;
//    }
//
//    @GetMapping(value = "byTag", params = {"offset", "limit", "tag"})
//    public ResponseEntity getPostTag(
//            @RequestParam(name = "offset") int offset,
//            @RequestParam(name = "limit") int limit,
//            @RequestParam(name = "tag") String tag)
//    {
//        return null;
//    }
//
//    @GetMapping(value = "moderation", params = {"offset", "limit", "status "})
//    public ResponseEntity getPostModeration(
//            @RequestParam(name = "offset") int offset,
//            @RequestParam(name = "limit") int limit,
//            @RequestParam(name = "status ") String status )
//    {
//        return null;
//    }
//
//    @GetMapping(value = "my", params = {"offset", "limit", "status "})
//    public ResponseEntity getPostMy(
//            @RequestParam(name = "offset") int offset,
//            @RequestParam(name = "limit") int limit,
//            @RequestParam(name = "status ") String status )
//    {
//        return null;
//    }
//
//    @GetMapping(value = "{id}}")
//    public ResponseEntity getPostId(@PathVariable int id)
//    {
//        return null;
//    }
//
//    @PostMapping(value = "")
//    public ResponseEntity post(@RequestBody PostRequest postRequest)
//    {
//        return null;
//    }
//
//    @PutMapping(value = "")
//    public ResponseEntity putPost(@PathVariable int id)
//    {
//        return null;
//    }
//
//    @PostMapping(value = "like")
//    public ResponseEntity likePost(@RequestBody LikeDislikeRequest likeDislikeRequest)
//    {
//        return null;
//    }
//
//    @PostMapping(value = "dislike")
//    public ResponseEntity dislikePost(@RequestBody LikeDislikeRequest likeDislikeRequest)
//    {
//        return null;
//    }
}