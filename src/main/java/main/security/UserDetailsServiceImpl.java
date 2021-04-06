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

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .getByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("user " + email + " " + "not found"));
    return SecurityUser.fromUser(user);
  }
}
