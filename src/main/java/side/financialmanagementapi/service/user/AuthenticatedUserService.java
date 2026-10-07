package side.financialmanagementapi.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.exceptions.UserNotFoundException;
import side.financialmanagementapi.repository.user.UserEntityRepository;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final UserEntityRepository userEntityRepository;

    public UserEntity getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return userEntityRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new UserNotFoundException("Authenticated user not found!")
                );
    }
}

//UserEntity user = authenticatedUserService.getAuthenticatedUser();