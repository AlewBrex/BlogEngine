package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.Main;
import main.api.request.LikeDislikeRequest;
import main.api.response.ResultResponse;
import main.api.response.result.FalseResultResponse;
import main.api.response.result.OkResultResponse;
import main.exception.LoginUserWrongCredentialsException;
import main.exception.NotPresentPost;
import main.model.Post;
import main.model.User;
import main.model.Vote;
import main.model.repository.PostRepository;
import main.model.repository.UserRepository;
import main.model.repository.VoteRepository;
import main.service.interfaces.VoteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  private final VoteRepository voteRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public ResultResponse likePost(LikeDislikeRequest likeDislikeRequest, Principal principal) {
    boolean like = setVote(likeDislikeRequest, 1, principal);
    return like ? new OkResultResponse() : new FalseResultResponse();
  }

  public ResultResponse dislikePost(LikeDislikeRequest likeDislikeRequest, Principal principal) {
    boolean dislike = setVote(likeDislikeRequest, -1, principal);
    return dislike ? new OkResultResponse() : new FalseResultResponse();
  }

  private boolean setVote(LikeDislikeRequest likeDislikeRequest, int voteValue, Principal principal)
      throws LoginUserWrongCredentialsException {
    User user;
    if (principal != null) {
      Optional<User> optionalUser = userRepository.getByEmail(principal.getName());
      if (optionalUser.isPresent()) {
        user = optionalUser.get();
        int id = likeDislikeRequest.getPostId();
        Post post = postRepository.getPostById(id).orElseThrow(NotPresentPost::new);

        LOGGER.info("User isn't authorized");
        Vote vote = voteRepository.getVoteByUserAndPost(user.getId(), post.getId());
        if (vote != null) {
          if (vote.getValue() != voteValue) {
            voteRepository.delete(vote);
          }
          if (vote.getValue() == voteValue) {
            return false;
          }
        }
        voteRepository.save(createNewVote(user, post, voteValue));
        return true;
      }
    }
    return false;
  }

  private Vote createNewVote(User user, Post post, Integer vote) {
    Vote firstVote = new Vote();
    firstVote.setUsers(user);
    firstVote.setPost(post);
    firstVote.setTime(LocalDateTime.now());
    firstVote.setValue(vote);
    return firstVote;
  }
}
