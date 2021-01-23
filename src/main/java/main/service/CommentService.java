package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.CommentRequest;
import main.api.response.IdResponse;
import main.model.Comment;
import main.model.Post;
import main.model.User;
import main.repository.CommentRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public IdResponse addComment(CommentRequest commentRequest, HttpSession httpSession) {
        Integer sessionId = Integer.valueOf(httpSession.getId());
        if (sessionId == null) {
            log.info("User isn't authorized");
        }
        User user = userRepository.findById(sessionId).get();
        int parentId = commentRequest.getParentId();
        int postId = commentRequest.getPostId();
        String text = commentRequest.getText();
        boolean textOk = text.isBlank() || text.length() < 50;
        if (textOk) {
            log.info("Message");
            return null;
        }
        Document textOfHtml = Jsoup.parse(text);
        String newText = textOfHtml.text();
        Comment comment = commentRepository.getOne(parentId);
        Post post = postRepository.getOne(postId);
        if (comment != null || post != null) {
            log.info("Message");
            return null;
        }
        Comment commentId = commentRepository.save(new Comment(comment, post, user, LocalDateTime.now(), newText));
        int id = commentId.getId();
        return new IdResponse(id);
    }
}