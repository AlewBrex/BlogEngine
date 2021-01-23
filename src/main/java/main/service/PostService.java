package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.CalendarResponse;
import main.api.response.post.CountPostResponse;
import main.api.response.post.FullInformPost;
import main.api.response.post.PostResponse;
import main.api.response.result.BadResultResponse;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.repository.PostRepository;
import main.repository.TagRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public CountPostResponse getPosts() {
        List<PostResponse> list = new ArrayList<>();
        list.addAll(postRepository.modeRecentAll());
        return new CountPostResponse(countPost(), list);
    }

    public CountPostResponse getPostsWithMode(String mode, int offset, int limit) {
        if (mode.isBlank()) {
            log.info("mode not set. Output of all posts");
            getPosts();
        }
        List<PostResponse> list = new ArrayList<>();
        switch (mode) {
            case "recent":
                list.addAll(postRepository.modeRecent(offset, limit));
                break;
            case "popular":
                list.addAll(postRepository.modePopular(offset, limit));
                break;
            case "best":
                list.addAll(postRepository.modeBest(offset, limit));
                break;
            case "early":
                list.addAll(postRepository.modeEarl(offset, limit));
                break;
        }
        return new CountPostResponse(countPost(), list);
    }

    public CountPostResponse searchPosts(int offset, int limit, String query) {
        if (query.isBlank()) {
            log.info("query not set. Output of all posts");
            getPosts();
        }
        int countPosts = postRepository.countSearchPosts(query);
        List<PostResponse> listSearch = postRepository.searchPosts(offset, limit, query);
        return new CountPostResponse(countPosts, listSearch);
    }

    public CountPostResponse getPostsByDate(int offset, int limit, String date) {
        int count = postRepository.countPostsByDate(date);
        List<PostResponse> listPostsByDate = postRepository.listPostsByDate(offset, limit, date);
        return new CountPostResponse(count, listPostsByDate);
    }

    public CountPostResponse getPostsByTags(int offset, int limit, String tag) {
        int count = postRepository.countPostsByTags(tag);
        List<PostResponse> listPostsByTag = postRepository.listPostsByDate(offset, limit, tag);
        return new CountPostResponse(count, listPostsByTag);
    }

    public CountPostResponse getPostsForModeration(int offset, int limit, String status, HttpSession httpSession) {
        Integer sessionId = Integer.valueOf(httpSession.getId());
        if (sessionId == null) {
            log.info("User isn't authorized");
        }
        User user = userRepository.findById(sessionId).get();
        if (user == null || user.getIsModerator() == 0) {
            log.info("Message");
        }
        List<PostResponse> postSet = new ArrayList<>();
        int countPostsForModeration = 0;
        int userId = user.getId();
        switch (status) {
            case "new":
                postSet.addAll(postRepository.postsForModeration(offset, limit, "NEW", userId));
                countPostsForModeration = postRepository.countPostsForModerationStatusNew();
                break;
            case "declined":
                postSet.addAll(postRepository.postsForModeration(offset, limit, "DECLINED", userId));
                countPostsForModeration = postRepository.countPostStatus(userId, "DECLINED");
                break;
            case "accepted":
                postSet.addAll(postRepository.postsForModeration(offset, limit, "ACCEPTED", userId));
                countPostsForModeration = postRepository.countPostStatus(userId, "ACCEPTED");
                break;
        }
        return new CountPostResponse(countPostsForModeration, postSet);
    }

    public CountPostResponse getMyPosts(int offset, int limit, String status, HttpSession httpSession) {
        Integer sessionId = Integer.valueOf(httpSession.getId());
        if (sessionId == null) {
            log.info("User isn't authorized");
        }
        User user = userRepository.findById(sessionId).get();
        if (user == null) {
            log.info("Message");
        }
        int userId = user.getId();
        List<PostResponse> postResponseList = new ArrayList<>();
        int countPosts = 0;
        switch (status) {
            case "inactive":
                postResponseList.addAll(postRepository.listUserPostInactive(offset, limit, userId));
                countPosts = postRepository.countPostInactive(userId);
                break;
            case "pending":
                postResponseList.addAll(postRepository.listUserPostStatus(offset, limit, userId, "NEW"));
                countPosts = postRepository.countPostStatus(userId, "NEW");
                break;
            case "declined":
                postResponseList.addAll(postRepository.listUserPostStatus(offset, limit, userId, "DECLINED"));
                countPosts = postRepository.countPostStatus(userId, "DECLINED");
                break;
            case "published":
                postResponseList.addAll(postRepository.listUserPostStatus(offset, limit, userId, "ACCEPTED"));
                countPosts = postRepository.countPostStatus(userId, "ACCEPTED");
                break;
        }
        return new CountPostResponse(countPosts, postResponseList);
    }

    public FullInformPost getPostsById(int id, HttpSession httpSession) {
        Post post = postRepository.getOne(id);
        if (post != null) {
            Integer sessionId = Integer.valueOf(httpSession.getId());
            if (sessionId == null) {
                log.info("User isn't authorized");
            }
            User user = userRepository.findById(sessionId).get();
            if (user == null || (user.getIsModerator() == 0) ||
                    (post.getUsers().getId() != user.getId())) {
                if (post.getIsActive() != 0 || post.getModerationStatus().equals(Post.ModerationStatus.ACCEPTED)
                        || post.getTime().isBefore(LocalDateTime.now())) {
                    post.setViewCount(post.getViewCount() + 1);
                    postRepository.save(post);
                    return new FullInformPost(post);
                }
            }
        }
        log.info("Message");
        return null;
    }

    public BadResultResponse addPost(PostRequest postRequest, HttpSession httpSession) {
        int active = postRequest.getActive();
        String title = postRequest.getTitle();
        Set<String> tags = postRequest.getTags();
        String text = postRequest.getText();
        long time = postRequest.getTimestamp();
        LocalDateTime localDateTime = Instant.EPOCH.plus(time, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (localDateTime.isAfter(LocalDateTime.now())) {
            localDateTime = LocalDateTime.now();
        }
        boolean titleOk = title.isBlank() || title.length() < 3;
        boolean textOk = text.isBlank() || text.length() < 50;
        if (titleOk || textOk) {
            log.info("Message");
            return new BadResultResponse(false);
        }
        Set<Tag> tagSet = new HashSet<>();
        for (String t : tags) {
            if (!t.isBlank()) {
                Tag tag = tagRepository.getTagByName(t);
                tagSet.add(tag);
            }
        }
        Integer sessionId = Integer.valueOf(httpSession.getId());
        if (sessionId == null) {
            log.info("User isn't authorized");
        }
        User user = userRepository.findById(sessionId).get();
        if (user == null) {
            log.info("Message");
            return null;
        }
        postRepository.save(new Post(active, user, localDateTime, title, text, tagSet));
        return new BadResultResponse(true);
    }

    public BadResultResponse editPost(int idPost, PostRequest postRequest, HttpSession httpSession) {
        Post post = postRepository.getOne(idPost);
        if (post == null) {
            log.info("Message");
            return new BadResultResponse(false);
        }
        int active = postRequest.getActive();
        String title = postRequest.getTitle();
        Set<String> tags = postRequest.getTags();
        String text = postRequest.getText();
        long time = postRequest.getTimestamp();
        LocalDateTime localDateTime = Instant.EPOCH.plus(time, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (localDateTime.isAfter(LocalDateTime.now())) {
            localDateTime = LocalDateTime.now();
        }
        boolean titleOk = title.isBlank() || title.length() < 3;
        boolean textOk = text.isBlank() || text.length() < 50;
        if (titleOk || textOk) {
            log.info("Message");
            return new BadResultResponse(false);
        }
        Integer sessionId = Integer.valueOf(httpSession.getId());
        if (sessionId == null) {
            log.info("User isn't authorized");
        }
        User user = userRepository.findById(sessionId).get();
        if (user == null) {
            log.info("Message");
            return new BadResultResponse(false);
        }
        Set<Tag> tagSet = new HashSet<>();
        for (String t : tags) {
            if (!t.isBlank()) {
                Tag tag = tagRepository.getTagByName(t);
                tagSet.add(tag);
            }
        }
        post.setIsActive(active);
        post.setTitle(title);
        post.setText(text);
        post.setTime(localDateTime);
        post.setTags(tagSet);
        postRepository.save(post);
        return new BadResultResponse(true);
    }

    public BadResultResponse moderatePost(ModerationRequest moderationRequest, HttpSession httpSession) {
        int postId = moderationRequest.getPostId();
        String decision = moderationRequest.getDecision();
        Post post = postRepository.getOne(postId);
        if (post == null) {
            log.info("Message");
            return new BadResultResponse(false);
        }
        Integer sessionId = Integer.valueOf(httpSession.getId());
        if (sessionId == null) {
            log.info("User isn't authorized");
        }
        User user = userRepository.findById(sessionId).get();
        if (user == null || user.getIsModerator() == 0) {
            log.info("Message");
            return new BadResultResponse(false);
        }
        Post.ModerationStatus moderationStatus = decision.equals("ACCEPTED") ? Post.ModerationStatus.ACCEPTED :
                Post.ModerationStatus.DECLINED;
        post.setIsModerator(user.getId());
        post.setModerationStatus(moderationStatus);
        postRepository.save(post);
        return new BadResultResponse(true);
    }

    public CalendarResponse postsByCalendar(Integer year) {
        int newYear = year == null ? LocalDateTime.now().getYear() : year;
        List<Integer> years = postRepository.yearsWithPosts();
        Map<String, Integer> posts = postRepository.daysCountPosts(newYear);
        log.info("Message");
        return new CalendarResponse(years, posts);
    }

    private int countPost() {
        return postRepository.countPostForModStatusAccepted();
    }

    public int countForModeration() {
        return postRepository.countPostsForModerationStatusNew();
    }
}