
package side.financialmanagementapi.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.request.user.LoginRequest;
import side.financialmanagementapi.dto.request.user.UserRequest;
import side.financialmanagementapi.dto.response.user.LoginResponse;
import side.financialmanagementapi.dto.response.user.UserResponse;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.exceptions.EmailAlreadyExistsException;
import side.financialmanagementapi.exceptions.UserNotFoundException;
import side.financialmanagementapi.repository.user.UserEntityRepository;
import side.financialmanagementapi.service.JwtService;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthenticatedUserService authenticatedUserService;

    //Cadastro
    public UserResponse cadastroUsuario(UserRequest request) {

        if (userEntityRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        UserEntity user = UserEntity.builder()
                .name(request.name())
                .email(request.email())
                .senhaHash(passwordEncoder.encode(request.senhaHash()))
                .build();

        UserEntity savedUser = userEntityRepository.save(user);

        return toResponse(savedUser);
    }

    //Atualizar
    public UserResponse atualizarUsuario(UserRequest request) {

        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        if (userEntityRepository.existsByEmailAndIdNot(
                request.email(),
                user.getId()
        )) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        user.setName(request.name());
        user.setEmail(request.email());


        user.setSenhaHash(passwordEncoder.encode(request.senhaHash()));

        UserEntity savedUser = userEntityRepository.save(user);

        return toResponse(savedUser);
    }

    //Excluir
    public void deletarUsuario() {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        user.setActive(false);

        userEntityRepository.save(user);
    }

    //Login publico
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.senha()
                        )
                );

        UserEntity user = userEntityRepository.findByEmail(
                authentication.getName()
        ).orElseThrow(() -> new UserNotFoundException("User not found!"));

        String token = jwtService.generateToken(
                user.getId().toString()
        );

        return new LoginResponse(token);
    }

    private UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}