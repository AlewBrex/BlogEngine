package main.service.interfaces;

import main.api.request.CommentRequest;
import main.api.response.ResultResponse;
import main.exception.EmptyTextComment;
import main.exception.LoginUserWrongCredentialsException;
import main.exception.UpSizeAtUploadImage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface CommentService {

    ResponseEntity<ResultResponse> addComment(CommentRequest req, Principal principal)
            throws LoginUserWrongCredentialsException, EmptyTextComment, UpSizeAtUploadImage,
            IllegalArgumentException;

    ResultResponse uploadImage(MultipartFile multipartFile, Principal principal)
            throws LoginUserWrongCredentialsException;
}
