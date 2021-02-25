package main.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.model.User;
import main.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG, email)));
        org.springframework.security.core.userdetails.User detailsUser =
                new org.springframework.security.core.userdetails.User(user.getEmail(),
                        user.getPassword(),
                        true, true, true, true,
                        user.getRole().getAuthorities());
        return detailsUser;
    }
}