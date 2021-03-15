package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.CommentRequest;
import main.api.response.IdResponse;
import main.api.response.ResultResponse;
import main.api.response.result.BadResultResponse;
import main.api.response.result.FalseResultResponse;
import main.exception.ContentNotAllowedException;
import main.model.Comment;
import main.model.Post;
import main.model.User;
import main.model.repository.CommentRepository;
import main.model.repository.PostRepository;
import main.model.repository.UserRepository;
import main.service.interfaces.CommentService;
import main.service.interfaces.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

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
    private final UserRepository userRepository;

    public ResultResponse addComment(CommentRequest req, Principal principal) throws UsernameNotFoundException, ContentNotAllowedException {
        BadResultResponse badResultResponse = new BadResultResponse();
        Optional<User> optionalUser = null;
        if (principal != null) {
            optionalUser = userRepository.getByEmail(principal.getName());
        }
        if (!optionalUser.isPresent()) {
            log.info("Пользователя не найден. Попробуйте зарегистрироваться.");
            throw new UsernameNotFoundException("Пользователь не зарегистрирован.");
        }
        User user = optionalUser.get();
        if (getTextOk(req.getText())) {
            log.warn("Short text or don't exist");
            badResultResponse.addError("text", "Tекст комментария не задан или слишком короткий");
            return new BadResultResponse(badResultResponse.getErrors());
        }

        Document textOfHtml = Jsoup.parse(req.getText());
        String newText = textOfHtml.text();
        Comment commentParent = commentRepository.findById(req.getParentId()).orElse(null);
        Post post = postRepository.getPostById(req.getPostId());

        if (commentParent == null && post == null) {
            log.warn("Don't exist parent comment or post");
            throw new ContentNotAllowedException().createWith("text", "Don't exist parent comment or post");
        }
        Comment newComment = new Comment(commentParent, post, user, LocalDateTime.now(), newText);
        commentRepository.save(newComment);;
        return new IdResponse(newComment.getId());
    }

    private Boolean getTextOk(String text) {
        return text.isBlank() || text.length() < minLengthComment ||
                text.length() > maxLengthComment;
    }
}