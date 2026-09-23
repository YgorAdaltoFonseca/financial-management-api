package side.financialmanagementapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.repository.user.UserEntityRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {


        UserEntity user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return (UserDetails) user;
    }
}
