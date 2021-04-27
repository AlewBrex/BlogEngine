package main.service.interfaces;

import main.api.request.LikeDislikeRequest;
import main.api.response.ResultResponse;

import java.security.Principal;

public interface VoteService {

  ResultResponse likePost(LikeDislikeRequest likeDislikeRequest, Principal principal);

  ResultResponse dislikePost(LikeDislikeRequest likeDislikeRequest, Principal principal);
}
