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
                SecurityContextHolder.getContext().getAuthentication();

        Long userId;

        try {
            userId = Long.parseLong(authentication.getName());
        } catch (NumberFormatException exception) {
            throw new UserNotFoundException("Invalid user identity in token");
        }

        return userEntityRepository.findById(userId)
                .filter(UserEntity::isEnabled)
                .orElseThrow(() ->
                        new UserNotFoundException("Authenticated user not found!")
                );
    }
}

//UserEntity user = authenticatedUserService.getAuthenticatedUser();