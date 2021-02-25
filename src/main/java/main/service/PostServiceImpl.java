package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.CalendarResponse;
import main.api.response.CommentResponse;
import main.api.response.ResultResponse;
import main.api.response.post.CountPostResponse;
import main.api.response.post.FullInformPost;
import main.api.response.post.ImageUploadResponse;
import main.api.response.post.PostResponse;
import main.api.response.result.BadResultResponse;
import main.api.response.result.FalseResultResponse;
import main.api.response.result.OkResultResponse;
import main.api.response.user.UserResponse;
import main.api.response.user.UserWithPhotoResponse;
import main.model.Comment;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.model.repository.CommentRepository;
import main.model.repository.PostRepository;
import main.model.repository.TagRepository;
import main.model.repository.VoteRepository;
import main.service.interfaces.PostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Value("${post.title.min_length}")
    private int minLengthTitle;
    @Value("${post.title.max_length}")
    private int maxLengthTitle;
    @Value("${post.text.min_length}")
    private int minLengthText;
    @Value("${post.text.max_length}")
    private int maxLengthText;
    @Value("${post.announce.max_length}")
    private int maxLengthAnnounce;

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final UserServiceImpl userService;
    private final ImageServiceImpl imageService;

    public ResultResponse getPosts() {
        List<Post> list = postRepository.modeRecentAll();
        List<PostResponse> responseList = getListPostResponse(list);
        return new CountPostResponse(countPost(), responseList);
    }

    public ResultResponse getPostsWithMode(String mode, int offset, int limit) {
        if (mode.isBlank()) {
            log.info("mode not set. Output of all posts");
            getPosts();
        }
        List<Post> list = new ArrayList<>();
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
        List<PostResponse> responseList = getListPostResponse(list);
        return new CountPostResponse(countPost(), responseList);
    }

    public ResultResponse searchPosts(int offset, int limit, String query) {
        if (query.isBlank()) {
            log.info("Query not set. Output of all posts");
            getPosts();
        }
        int countPosts = postRepository.countSearchPosts(query).orElse(0);
        List<Post> listSearch = postRepository.searchPosts(offset, limit, query);
        List<PostResponse> responseList = getListPostResponse(listSearch);
        return new CountPostResponse(countPosts, responseList);
    }

    public ResultResponse getPostsByDate(int offset, int limit, String date) {
        int count = postRepository.countPostsByDate(date).orElse(0);
        List<Post> listPostsByDate = postRepository.listPostsByDate(offset, limit, date);
        List<PostResponse> responseList = getListPostResponse(listPostsByDate);
        return new CountPostResponse(count, responseList);
    }

    public ResultResponse getPostsByTags(int offset, int limit, String tag) {
        int count = postRepository.countPostsByTags(tag).orElse(0);
        List<Post> listPostsByTag = postRepository.listPostByTags(offset, limit, tag);
        List<PostResponse> responseList = getListPostResponse(listPostsByTag);
        return new CountPostResponse(count, responseList);
    }

    public ResultResponse getPostsForModeration(int offset, int limit, String status, Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        if (user == null || user.getIsModerator() == 0) {
            log.info("User isn't authorized");
        }
        List<Post> postSet = new ArrayList<>();
        int countPostsForModeration = 0;
        int userId = user.getId();
        switch (status) {
            case "new":
                postSet.addAll(postRepository.postsForModeration(offset, limit, "NEW", userId));
                countPostsForModeration = postRepository.countPostsForModerationStatusNew().orElse(0);
                break;
            case "declined":
                postSet.addAll(postRepository.postsForModeration(offset, limit, "DECLINED", userId));
                countPostsForModeration = postRepository.countPostStatus(userId, "DECLINED").orElse(0);
                break;
            case "accepted":
                postSet.addAll(postRepository.postsForModeration(offset, limit, "ACCEPTED", userId));
                countPostsForModeration = postRepository.countPostStatus(userId, "ACCEPTED").orElse(0);
                break;
        }
        List<PostResponse> responseList = getListPostResponse(postSet);
        return new CountPostResponse(countPostsForModeration, responseList);
    }

    public ResultResponse getMyPosts(int offset, int limit, String status, Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        if (user == null) {
            log.info("User isn't authorized");
        }
        int userId = user.getId();
        List<Post> postResponseList = new ArrayList<>();
        int countPosts = 0;
        switch (status) {
            case "inactive":
                postResponseList.addAll(postRepository.listUserPostInactive(offset, limit, userId));
                countPosts = postRepository.countPostInactive(userId);
                break;
            case "pending":
                postResponseList.addAll(postRepository.listUserPostStatus(offset, limit, userId, "NEW"));
                countPosts = postRepository.countPostStatus(userId, "NEW").orElse(0);
                break;
            case "declined":
                postResponseList.addAll(postRepository.listUserPostStatus(offset, limit, userId, "DECLINED"));
                countPosts = postRepository.countPostStatus(userId, "DECLINED").orElse(0);
                break;
            case "published":
                postResponseList.addAll(postRepository.listUserPostStatus(offset, limit, userId, "ACCEPTED"));
                countPosts = postRepository.countPostStatus(userId, "ACCEPTED").orElse(0);
                break;
        }
        List<PostResponse> responseList = getListPostResponse(postResponseList);
        return new CountPostResponse(countPosts, responseList);
    }

    public ResultResponse getPostsById(int id, Principal principal) {
        Post post = postRepository.getPostById(id);
        User user = userService.getCurrentUser(principal.getName());
        if (user == null || ((user.getIsModerator() == 0) && (post.getUsers().getId() != user.getId()))) {
            post.setViewCount(post.getViewCount() + 1);
            return getPostForUser(post);
        }
        return getPostForUser(post);
    }

    public ResultResponse addPost(PostRequest postRequest, Principal principal) {
        BadResultResponse badResultResponse = new BadResultResponse();
        int active = postRequest.getActive();
        String title = postRequest.getTitle();
        Set<String> tags = postRequest.getTags();
        String text = postRequest.getText();
        long time = postRequest.getTimestamp();
        boolean titleOk = title.isBlank() || title.length() < minLengthTitle ||
                title.length() > maxLengthTitle;
        ;
        boolean textOk = text.isBlank() || text.length() < minLengthText ||
                text.length() > maxLengthText;
        ;
        User user = userService.getCurrentUser(principal.getName());
        LocalDateTime localDateTime =
                Instant.EPOCH.plus(time, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<Tag> tagSet = new ArrayList<>();
        for (String t : tags) {
            if (!t.isBlank()) {
                Tag tag = tagRepository.getTagByName(t);
                tagSet.add(tag);
            }
        }
        if (localDateTime.isAfter(LocalDateTime.now())) {
            localDateTime = LocalDateTime.now();
        }
        if (titleOk) {
            badResultResponse.addError("title", "Заголовок слишком короткий");
        }
        if (textOk) {
            badResultResponse.addError("text", "Текст публикации слишком короткий");
        }
        if (user == null) {
            log.warn("User isn't authorized or user don't exist");
        }

        if (badResultResponse.getErrors().size() > 0) {
            return badResultResponse;
        } else {
            Post post = new Post();
            post.setIsActive(active);
            post.setUsers(user);
            post.setTime(localDateTime);
            post.setTitle(title);
            post.setText(text);
            post.setTags(tagSet);
            post.setModerationStatus(Post.ModerationStatus.NEW);
            postRepository.save(post);
            return new OkResultResponse();
        }
    }

    public ResultResponse editPost(int idPost, PostRequest postRequest, Principal principal) {
        BadResultResponse badResultResponse = new BadResultResponse();
        Post post = postRepository.getPostById(idPost);
        boolean postNull = post == null;
        if (postNull) {
            log.info("Post don't exist");
        }
        int active = postRequest.getActive();
        String title = postRequest.getTitle();
        Set<String> tags = postRequest.getTags();
        String text = postRequest.getText();
        long time = postRequest.getTimestamp();
        LocalDateTime localDateTime =
                Instant.EPOCH.plus(time, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDateTime();
        User user = userService.getCurrentUser(principal.getName());
        boolean titleOk = title.isBlank() || title.length() < minLengthTitle ||
                title.length() > maxLengthTitle;
        boolean textOk = text.isBlank() || text.length() < minLengthText ||
                text.length() > maxLengthText;
        boolean userNull = user == null || user.getIsModerator() == 0;
        List<Tag> tagSet = new ArrayList<>();
        for (String t : tags) {
            if (!t.isBlank()) {
                Tag tag = tagRepository.getTagByName(t);
                tagSet.add(tag);
            }
        }
        if (localDateTime.isAfter(LocalDateTime.now())) {
            localDateTime = LocalDateTime.now();
        }
        if (titleOk) {
            badResultResponse.addError("title", "Заголовок слишком короткий");
        }
        if (textOk) {
            badResultResponse.addError("text", "Текст публикации слишком короткий");
        }
        if (userNull) {
            log.warn("User isn't authorized or user don't exist");
        }

        if (badResultResponse.getErrors().size() > 0) {
            return badResultResponse;
        } else {
            post.setIsActive(active);
            post.setTitle(title);
            post.setText(text);
            post.setTime(localDateTime);
            post.setTags(tagSet);
            post.setModerationStatus(user.getIsModerator() == 0 ? Post.ModerationStatus.NEW :
                    Post.ModerationStatus.ACCEPTED);
            postRepository.save(post);
            return new OkResultResponse();
        }
    }

    public ResultResponse uploadImage(MultipartFile multipartFile, Principal principal) {
        if (multipartFile.isEmpty()) {
            log.warn("Don't exist image for upload");
        }
        User user = userService.getCurrentUser(principal.getName());
        if (user == null) {
            log.warn("User isn't authorized");
        }
        String path = imageService.uploadFile(multipartFile);
        return new ImageUploadResponse(path);
    }

    public ResultResponse moderatePost(ModerationRequest moderationRequest, Principal principal) {
        int postId = moderationRequest.getPostId();
        String decision = moderationRequest.getDecision();
        Post post = postRepository.getOne(postId);
        User user = userService.getCurrentUser(principal.getName());
        boolean postNull = post == null;
        boolean userNull = user == null || user.getIsModerator() == 0;
        if (postNull) {
            log.warn("Post don't exist");
        }
        if (userNull) {
            log.warn("User isn't authorized or user don't exist");
        }

        if (post == null || userNull) {
            return new FalseResultResponse();
        } else {
            post.setModeratorId(user.getId());
            post.setModerationStatus(decision.equals("ACCEPTED") ? Post.ModerationStatus.ACCEPTED :
                    Post.ModerationStatus.DECLINED);
            postRepository.save(post);
            return new OkResultResponse();
        }
    }

    public CalendarResponse postsByCalendar(Integer year) {
        int newYear = year == null ? LocalDateTime.now().getYear() : year;
        List<Integer> years = postRepository.yearsWithPosts();
        List<Object[]> posts = postRepository.daysCountPosts(newYear);
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        posts.forEach(f -> {
            String datePosts = f[0].toString();
            Integer countPosts = ((BigInteger) f[1]).intValue();
            stringIntegerMap.put(datePosts, countPosts);
        });
        return new CalendarResponse(years, stringIntegerMap);
    }

    private int countPost() {
        return postRepository.countPostForModStatusAccepted().orElse(0);
    }

    private List<PostResponse> getListPostResponse(List<Post> postList) {
        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post p : postList) {
            int postId = p.getId();
            int likeCount = voteRepository.getLikeByPostId(postId);
            int dislikeCount = voteRepository.getDislikeByPostId(postId);
            int commentCount = commentRepository.getCountCommentsByPostId(postId);
            UserResponse userResponse = new UserResponse();
            userResponse.setId(p.getUsers().getId());
            userResponse.setName(p.getUsers().getName());
            postResponseList.add(new PostResponse(
                    postId,
                    p.getTime().atZone(ZoneId.systemDefault()).toEpochSecond(),
                    userResponse,
                    p.getTitle(),
                    getAnnounce(p.getText()),
                    likeCount,
                    dislikeCount,
                    commentCount,
                    p.getViewCount()));
        }
        return postResponseList;
    }

    private String getAnnounce(String text) {
        int firstSpace = text.lastIndexOf(" ");
        String announce = text.length() > maxLengthAnnounce ? text.substring(0, firstSpace).concat("...") : text;
        return announce;
    }

    private FullInformPost getPostForUser(Post post) {
        if (post.getIsActive() != 0 || post.getModerationStatus().equals(Post.ModerationStatus.ACCEPTED)
                || post.getTime().isBefore(LocalDateTime.now())) {
            int idPost = post.getId();
            long timestamp = post.getTime().atZone(ZoneId.systemDefault()).toEpochSecond();
            boolean active = post.getIsActive() == 1;
            UserResponse userPost = new UserResponse();
            userPost.setId(post.getUsers().getId());
            userPost.setName(post.getUsers().getName());
            String title = post.getTitle();
            String text = post.getText();
            String announce = getAnnounce(post.getText());
            int likeCount = voteRepository.getLikeByPostId(post.getId());
            int dislikeCount = voteRepository.getDislikeByPostId(post.getId());
            int commentCount = commentRepository.getCountCommentsByPostId(idPost);
            int viewCount = post.getViewCount();
            List<CommentResponse> comments = getListComments(post);
            List<String> tags = getListTags(post);
            postRepository.save(post);
            return new FullInformPost(
                    idPost,
                    timestamp,
                    active,
                    userPost,
                    title,
                    text,
                    announce,
                    likeCount,
                    dislikeCount,
                    commentCount,
                    viewCount,
                    comments,
                    tags);
        }
        return null;
    }

    private List<CommentResponse> getListComments(Post post) {
        List<CommentResponse> comments = new ArrayList<>();
        List<Comment> list = new ArrayList<>();
        list.addAll(commentRepository.getListCommentsByPostId(post.getId()));
        for (int i = 0; i < list.size(); i++) {
            Comment c = list.get(i);
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setId(c.getId());
            commentResponse.setTimestamp(c.getTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            commentResponse.setText(c.getText());
            UserWithPhotoResponse userWithPhotoResponse = new UserWithPhotoResponse();
            userWithPhotoResponse.setId(c.getUsers().getId());
            userWithPhotoResponse.setName(c.getUsers().getName());
            userWithPhotoResponse.setPhoto(c.getUsers().getPhoto());
            commentResponse.setUser(userWithPhotoResponse);
            comments.add(commentResponse);
        }
        return comments;
    }

    private List<String> getListTags(Post post) {
        List<String> tags = new ArrayList<>();
        for (Tag t : post.getTags()) {
            String tagPost = t.getName();
            tags.add(tagPost);
        }
        return tags;
    }
}