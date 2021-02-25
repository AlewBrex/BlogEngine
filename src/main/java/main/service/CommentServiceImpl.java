package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.CommentRequest;
import main.api.response.IdResponse;
import main.model.Comment;
import main.model.Post;
import main.model.User;
import main.model.repository.CommentRepository;
import main.model.repository.PostRepository;
import main.service.interfaces.CommentService;
import main.service.interfaces.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Value("${comment.min_length}")
    private int minLengthComment;
    @Value("${comment.max_length}")
    private int maxLengthComment;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserServiceImpl userService;

    public IdResponse addComment(CommentRequest commentRequest, Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        int parentId = commentRequest.getParentId();
        int postId = commentRequest.getPostId();
        String text = commentRequest.getText();
        boolean textOk = text.isBlank() || text.length() < minLengthComment ||
                text.length() > maxLengthComment;
        if (textOk) {
            log.warn("Short text or don't exist");
        }
        Document textOfHtml = Jsoup.parse(text);
        String newText = textOfHtml.text();
        Comment commentParent = commentRepository.findById(parentId).orElse(null);
        Post post = postRepository.getPostById(postId);
        if (commentParent == null && post == null) {
            log.warn("Don't exist parent comment or post");
        }
        Comment newComment = new Comment();
        newComment.setParent(commentParent);
        newComment.setPost(post);
        newComment.setUsers(user);
        newComment.setTime(LocalDateTime.now());
        newComment.setText(newText);
        commentRepository.save(newComment);
        int id = newComment.getId();
        return new IdResponse(id);
    }
}