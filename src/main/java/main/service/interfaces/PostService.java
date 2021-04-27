package main.service.interfaces;

import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.ResultResponse;
import main.exception.LoginUserWrongCredentialsException;
import main.exception.NotPresentPost;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface PostService {
  ResultResponse getPosts();

  ResultResponse getPostsWithMode(String mode, int offset, int limit);

  ResultResponse searchPosts(int offset, int limit, String query);

  ResultResponse getPostsByDate(int offset, int limit, String date);

  ResultResponse getPostsByTags(int offset, int limit, String tag);

  ResultResponse getPostsForModeration(int offset, int limit, String status, Principal principal)
      throws LoginUserWrongCredentialsException;

  ResultResponse getMyPosts(int offset, int limit, String status, Principal principal)
      throws LoginUserWrongCredentialsException;

  ResultResponse getPostsById(int id, Principal principal) throws NotPresentPost;

  ResultResponse addPost(PostRequest req, Principal principal);

  ResultResponse editPost(int idPost, PostRequest req, Principal principal);

  ResultResponse uploadImage(MultipartFile multipartFile, Principal principal)
      throws LoginUserWrongCredentialsException;

  ResultResponse moderatePost(ModerationRequest req, Principal principal);

  ResultResponse postsByCalendar(Integer year);
}
