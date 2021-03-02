package main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import main.api.request.LikeDislikeRequest;
import main.api.response.ResultResponse;
import main.api.response.result.FalseResultResponse;
import main.api.response.result.OkResultResponse;
import main.model.Post;
import main.model.User;
import main.model.Vote;
import main.model.repository.PostRepository;
import main.model.repository.VoteRepository;
import main.service.interfaces.VoteService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserServiceImpl userServiceImpl;

    public ResultResponse likePost(LikeDislikeRequest likeDislikeRequest, Principal principal) {
        boolean like = setVote(likeDislikeRequest, 1, principal);
        return like ? new OkResultResponse() : new FalseResultResponse();
    }

    public ResultResponse dislikePost(LikeDislikeRequest likeDislikeRequest, Principal principal) {
        boolean dislike = setVote(likeDislikeRequest, -1, principal);
        return dislike ? new OkResultResponse() : new FalseResultResponse();
    }

    private boolean setVote(LikeDislikeRequest likeDislikeRequest, int voteValue, Principal principal) {
        int id = likeDislikeRequest.getPostId();
        Post post = postRepository.getPostById(id);
        User user = userServiceImpl.getCurrentUserByEmail(principal.getName());
        if (user == null) {
            log.error("User isn't authorized");
        }
        Vote vote = voteRepository.getVoteByUserAndPost(user.getId(), post.getId());

        if (vote == null || vote.getValue() != voteValue) {
            voteRepository.delete(vote);
            getNewVote(user, post, voteValue);
        }
        return false;
    }

    private Boolean getNewVote(User user, Post post, Integer vote) {
        Vote firstVote = new Vote();
        firstVote.setUsers(user);
        firstVote.setPost(post);
        firstVote.setTime(LocalDateTime.now());
        firstVote.setValue(vote);
        voteRepository.save(firstVote);
        return true;
    }
}