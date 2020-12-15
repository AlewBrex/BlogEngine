package main.service;

import lombok.extern.log4j.Log4j2;
import main.api.response.result.LoginResultResponse;
import main.api.response.result.OkResultResponse;
import main.api.response.user.AllUserInformation;
import main.api.response.user.UserWithPhoto;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class UserService
{
    private Map<String, Integer> sessionIdAndUserId = new HashMap<>();

    private final UserRepository userRepository;
    private final PostService postService;

    @Autowired
    public UserService(UserRepository userRepository, PostService postService)
    {
        this.userRepository = userRepository;
        this.postService = postService;
    }

    public LoginResultResponse check(HttpSession httpSession)
    {
        String session = httpSession.getId();
        if (!sessionIdAndUserId.containsKey(session))
        {
            log.info("Message");
        }
        Integer userId = sessionIdAndUserId.get(session);
        User user = userRepository.findById(userId).get();

        return new LoginResultResponse(true, new AllUserInformation(
                new UserWithPhoto(user.getId(),
                        user.getName(),
                        user.getPhoto()),
                user.getEmail(),
                itTrue(user),
                postService.countPostsForModeration(),
                itTrue(user)
        ));
    }

    public OkResultResponse logout(HttpSession httpSession)
    {
        String session = httpSession.getId();
        if (!sessionIdAndUserId.containsKey(session))
        {
            log.info("Message");
        }
        sessionIdAndUserId.remove(session);
        return new OkResultResponse(true);
    }

    private boolean itTrue(User user)
    {
        return user.getIsModerator() == 1;
    }
}