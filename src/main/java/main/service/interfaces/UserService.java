package main.service.interfaces;

import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestoreRequest;
import main.api.request.change.ChangeDataMyProfile;
import main.api.request.change.ChangePasswordRequest;
import main.api.response.ResultResponse;
import main.exception.ContentNotAllowedException;
import main.exception.LoginUserWrongCredentialsException;
import main.exception.WrongParameterException;
import main.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public interface UserService {

  User getCurrentUserByEmail(String email);

  ResultResponse editMyProfile(ChangeDataMyProfile change, Principal principal);

  ResultResponse registerUser(RegisterRequest req) throws ContentNotAllowedException;

  ResultResponse changePassword(ChangePasswordRequest req) throws UsernameNotFoundException;

  ResultResponse restorePassword(RestoreRequest req) throws LoginUserWrongCredentialsException;

  ResultResponse allStatistics(Principal principal)
      throws LoginUserWrongCredentialsException, WrongParameterException;

  ResultResponse myStatistics(Principal principal)
      throws LoginUserWrongCredentialsException, IllegalStateException;

  ResultResponse logout();

  ResultResponse login(LoginRequest req)
      throws LoginUserWrongCredentialsException, IllegalStateException;

  ResultResponse check(Principal principal);
}
