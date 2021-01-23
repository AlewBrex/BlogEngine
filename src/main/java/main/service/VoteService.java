package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.LikeDislikeRequest;
import main.api.response.result.OkResultResponse;
import main.model.Post;
import main.model.User;
import main.model.Vote;
import main.repository.PostRepository;
import main.repository.UserRepository;
import main.repository.VoteRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public OkResultResponse likePost(LikeDislikeRequest likeDislikeRequest, HttpSession httpSession) {
        boolean likeBoolean = setVote(likeDislikeRequest, 1, httpSession);
        return new OkResultResponse(likeBoolean);
    }

    public OkResultResponse dislikePost(LikeDislikeRequest likeDislikeRequest, HttpSession httpSession) {
        boolean dislikeBoolean = setVote(likeDislikeRequest, -1, httpSession);
        return new OkResultResponse(dislikeBoolean);
    }

    private boolean setVote(LikeDislikeRequest dislikeRequest, int voteValue, HttpSession httpSession) {
        int id = dislikeRequest.getPostId();
        Post post = postRepository.getOne(id);
        Integer sessionId = Integer.valueOf(httpSession.getId());
        if (sessionId == null) {
            log.info("User isn't authorized");
        }
        User user = userRepository.findById(sessionId).get();
        Vote vote = voteRepository.getVoteByUserAndPost(user.getId(), id);
        boolean like = vote.getValue() != voteValue;
        if (vote == null || like) {
            voteRepository.save(new Vote(user, post, LocalDateTime.now(), voteValue));
            return true;
        }
        return false;
    }
}