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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public IdResponse addComment(CommentRequest req, Principal principal) {
        User user = userService.getCurrentUserByEmail(principal.getName());

        boolean textOk = req.getText().isBlank() || req.getText().length() < minLengthComment ||
                req.getText().length() > maxLengthComment;

        if (textOk) {
            log.warn("Short text or don't exist");
        }

        Document textOfHtml = Jsoup.parse(req.getText());
        String newText = textOfHtml.text();
        Comment commentParent = commentRepository.findById(req.getParentId()).orElse(null);
        Post post = postRepository.getPostById(req.getPostId());

        if (commentParent == null && post == null) {
            log.warn("Don't exist parent comment or post");
        }
        Comment newComment = new Comment(commentParent, post, user, LocalDateTime.now(), newText);
        commentRepository.save(newComment);;
        return new IdResponse(newComment.getId());
    }
}