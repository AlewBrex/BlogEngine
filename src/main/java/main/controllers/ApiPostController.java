package main.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.LikeDislikeRequest;
import main.api.request.PostRequest;
import main.service.PostServiceImpl;
import main.service.VoteServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Log4j2
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostServiceImpl postServiceImpl;
    private final VoteServiceImpl voteServiceImpl;

    @GetMapping()
    public ResponseEntity<?> getPosts() {
        log.info("Get all posts");
        return new ResponseEntity<>(postServiceImpl.getPosts(), HttpStatus.OK);
    }

    @GetMapping(value = "", params = {"offset", "limit", "mode"})
    public ResponseEntity<?> getPosts(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "mode") String mode) {
        log.info("Get all posts with parameters: offset {} + limit {} + mode {}", offset, limit, mode);
        return new ResponseEntity<>(postServiceImpl.getPostsWithMode(mode, offset, limit), HttpStatus.OK);
    }

    @GetMapping(value = "search")
    public ResponseEntity<?> searchPost(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "query") String query) {
        log.info("Received all posts with parameters: offset {} + limit {} + query {}", offset, limit, query);
        return new ResponseEntity<>(postServiceImpl.searchPosts(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping(value = "byDate")
    public ResponseEntity<?> getPostByDate(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "date") String date) {
        log.info("Get all posts by date with parameters: offset {} + limit {} + date {}", offset, limit, date);
        return new ResponseEntity<>(postServiceImpl.getPostsByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping(value = "byTag")
    public ResponseEntity<?> getPostTag(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "tag") String tag) {
        log.info("Get all posts by tag with parameters: offset {} + limit {} + tag {}", offset, limit, tag);
        return new ResponseEntity<>(postServiceImpl.getPostsByTags(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping(value = "moderation")
    public ResponseEntity<?> getPostModeration(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "status") String status,
            Principal principal) {
        log.info("Get all posts for moderation with parameters: offset {} + limit {} + status {}", offset, limit,
                status);
        return new ResponseEntity<>(postServiceImpl.getPostsForModeration(offset, limit, status, principal),
                HttpStatus.OK);
    }

    @GetMapping(value = "my")
    public ResponseEntity<?> getMePosts(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "status") String status,
            Principal principal) {
        log.info("Get all my posts with parameters: offset {} + limit {} + status {}", offset, limit, status);
        return new ResponseEntity<>(postServiceImpl.getMyPosts(offset, limit, status, principal), HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getPostId(@PathVariable int id, Principal principal) {
        log.info("Get post by id");
        return new ResponseEntity<>(postServiceImpl.getPostsById(id, principal), HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> addPost(@RequestBody PostRequest postRequest, Principal principal) {
        log.info("Add new post");
        return new ResponseEntity<>(postServiceImpl.addPost(postRequest, principal), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> editPost(@PathVariable int id, @RequestBody PostRequest postRequest,
                                      Principal principal) {
        log.info("Edit post");
        return new ResponseEntity<>(postServiceImpl.editPost(id, postRequest, principal), HttpStatus.OK);
    }

    @PostMapping(value = "like")
    public ResponseEntity<?> likePost(@RequestBody LikeDislikeRequest likeDislikeRequest,
                                      Principal principal) {
        log.info("Like");
        return new ResponseEntity<>(voteServiceImpl.likePost(likeDislikeRequest, principal), HttpStatus.OK);
    }

    @PostMapping(value = "dislike")
    public ResponseEntity<?> dislikePost(@RequestBody LikeDislikeRequest likeDislikeRequest,
                                         Principal principal) {
        log.info("Dislike");
        return new ResponseEntity<>(voteServiceImpl.dislikePost(likeDislikeRequest, principal), HttpStatus.OK);
    }
}