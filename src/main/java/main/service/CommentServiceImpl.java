package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.Main;
import main.api.request.CommentRequest;
import main.api.response.IdResponse;
import main.api.response.ResultResponse;
import main.api.response.result.BadResultResponse;
import main.exception.EmptyTextComment;
import main.exception.LoginUserWrongCredentialsException;
import main.exception.NotPresentPost;
import main.model.Comment;
import main.model.Post;
import main.model.User;
import main.model.repository.CommentRepository;
import main.model.repository.PostRepository;
import main.model.repository.UserRepository;
import main.service.interfaces.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  @Value("${comment.min_length}")
  private int minLengthComment;

  @Value("${comment.max_length}")
  private int maxLengthComment;

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public ResultResponse addComment(CommentRequest req, Principal principal)
      throws LoginUserWrongCredentialsException, EmptyTextComment {
    BadResultResponse badResultResponse = new BadResultResponse();
    User user = null;
    if (principal == null) {
      throw new LoginUserWrongCredentialsException();
    }
    Optional<User> optionalUser = userRepository.getByEmail(principal.getName());
    if (optionalUser.isPresent()) {
      user = optionalUser.get();
    }
    if (getTextNotOk(req.getText())) {
      LOGGER.warn("Short text or don't exist");
      badResultResponse.addError("text", "Tекст комментария не задан или слишком короткий");
      return new BadResultResponse(badResultResponse.getErrors());
    }

    Document textOfHtml = Jsoup.parse(req.getText());
    String newText = textOfHtml.text();
    Comment commentParent = commentRepository.findById(req.getParentId()).orElse(null);
    Post post = postRepository.getPostById(req.getPostId()).orElseThrow(NotPresentPost::new);

    if (commentParent == null && post == null) {
      LOGGER.warn("Don't exist parent comment or post");
      throw new EmptyTextComment();
    }
    Comment newComment = new Comment(commentParent, post, user, LocalDateTime.now(), newText);
    commentRepository.save(newComment);
    return new IdResponse(newComment.getId());
  }

  private Boolean getTextNotOk(String text) {
    return text.isBlank() || text.length() < minLengthComment || text.length() > maxLengthComment;
  }
}
