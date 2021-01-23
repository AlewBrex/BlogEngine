package main.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.LikeDislikeRequest;
import main.api.request.PostRequest;
import main.service.PostService;
import main.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Log4j2
@RestController
@RequestMapping("/api/post/")
@RequiredArgsConstructor
public class ApiPostController {
    private final PostService postService;
    private final VoteService voteService;

    @GetMapping()
    public ResponseEntity<?> getPosts() {
        log.info("Get all posts");
        return new ResponseEntity<>(postService.getPosts(), HttpStatus.OK);
    }

    @GetMapping(value = "", params = {"offset", "limit", "mode"})
    public ResponseEntity<?> getPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "mode") String mode) {
        log.info("Get all posts with parameters: offset{} + limit{} + mode{}", offset, limit, mode);
        return new ResponseEntity<>(postService.getPostsWithMode(mode, offset, limit), HttpStatus.OK);
    }

    @GetMapping(value = "search", params = {"offset", "limit", "query"})
    public ResponseEntity<?> searchPost(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "query") String query) {
        log.info("Message");
        return new ResponseEntity<>(postService.searchPosts(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping(value = "byDate", params = {"offset", "limit", "date"})
    public ResponseEntity<?> getPostByDate(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "date") String date) {
        log.info("Message");
        return new ResponseEntity<>(postService.getPostsByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping(value = "byTag", params = {"offset", "limit", "tag"})
    public ResponseEntity<?> getPostTag(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "tag") String tag) {
        log.info("Message");
        return new ResponseEntity<>(postService.getPostsByTags(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping(value = "moderation", params = {"offset", "limit", "status"})
    public ResponseEntity<?> getPostModeration(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "status") String status,
            HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(postService.getPostsForModeration(offset, limit, status, httpSession),
                HttpStatus.OK);
    }

    @GetMapping(value = "my", params = {"offset", "limit", "status"})
    public ResponseEntity<?> getMyPosts(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "status") String status,
            HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(postService.getMyPosts(offset, limit, status, httpSession), HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getPostId(@PathVariable int id, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(postService.getPostsById(id, httpSession), HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> addPost(@RequestBody PostRequest postRequest, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(postService.addPost(postRequest, httpSession), HttpStatus.OK);
    }

    @PutMapping(value = "")
    public ResponseEntity<?> putPost(@PathVariable int id, @RequestBody PostRequest postRequest, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(postService.editPost(id, postRequest, httpSession), HttpStatus.OK);
    }

    @PostMapping(value = "like")
    public ResponseEntity<?> likePost(@RequestBody LikeDislikeRequest likeDislikeRequest, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(voteService.likePost(likeDislikeRequest, httpSession), HttpStatus.OK);
    }

    @PostMapping(value = "dislike")
    public ResponseEntity<?> dislikePost(@RequestBody LikeDislikeRequest likeDislikeRequest, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        log.info("Message");
        return new ResponseEntity<>(voteService.dislikePost(likeDislikeRequest, httpSession), HttpStatus.OK);
    }
}