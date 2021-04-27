package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.Main;
import main.api.request.CommentRequest;
import main.api.response.IdResponse;
import main.api.response.ResultResponse;
import main.api.response.post.ImageUploadResponse;
import main.api.response.result.BadResultResponse;
import main.exception.EmptyTextComment;
import main.exception.LoginUserWrongCredentialsException;
import main.exception.NotPresentPost;
import main.exception.UpSizeAtUploadImage;
import main.model.Comment;
import main.model.Post;
import main.model.User;
import main.model.repository.CommentRepository;
import main.model.repository.PostRepository;
import main.model.repository.UserRepository;
import main.service.interfaces.CommentService;
import main.service.interfaces.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
  private final ImageService imageService;

  @Override
  public ResponseEntity<ResultResponse> addComment(CommentRequest req, Principal principal)
      throws LoginUserWrongCredentialsException, EmptyTextComment, UpSizeAtUploadImage,
          IllegalArgumentException {
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
    }

    Post post = postRepository.getPostById(req.getPostId()).orElseThrow(NotPresentPost::new);

    Comment newComment;
    if (req.getParentId() == null) {
      newComment = new Comment(post, user, LocalDateTime.now(), req.getText());
      LOGGER.warn("Don't exist parent comment or post");
    } else {
      Optional<Comment> commentParent = commentRepository.findById(req.getParentId());
      newComment = new Comment(commentParent.get(), post, user, LocalDateTime.now(), req.getText());
    }

    if (badResultResponse.hasErrors()) {
      return new ResponseEntity<>(badResultResponse, HttpStatus.BAD_REQUEST);
    }
    commentRepository.save(newComment);
    return new ResponseEntity<>(new IdResponse(newComment.getId()), HttpStatus.OK);
  }

  @Override
  public ResultResponse uploadImage(MultipartFile multipartFile, Principal principal)
      throws LoginUserWrongCredentialsException {

    if (multipartFile.isEmpty()) {
      LOGGER.warn("Don't exist image for upload");
    }
    if (principal != null) {
      return new ImageUploadResponse(imageService.uploadFileAndResizeImage(multipartFile, false));
    }
    LOGGER.info("User isn't authorized");
    throw new LoginUserWrongCredentialsException();
  }

  private Boolean getTextNotOk(String text) {
    return text.isBlank() || text.length() < minLengthComment || text.length() > maxLengthComment;
  }
}
