package side.financialmanagementapi.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    // CADASTRAR USUARIO
    public UserResponse cadastroUsuario(UserRequest requestUser) {

        if (userEntityRepository.existsByEmail(requestUser.email())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        String senhaHash = passwordEncoder.encode(requestUser.senhaHash());

        UserEntity userEntity = UserEntity.builder()
                .name(requestUser.name())
                .email(requestUser.email())
                .senhaHash(senhaHash)
                .build();

        UserEntity savedUserEntity = userEntityRepository.save(userEntity);

        return new UserResponse(
                savedUserEntity.getId(),
                savedUserEntity.getName(),
                savedUserEntity.getEmail()
        );
    }

    //ATUALIZAR USUARIO
    public UserResponse atualizarUsuario(Long id, UserRequest requestUser) {

        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        userEntity.setName(requestUser.name());
        userEntity.setEmail(requestUser.email());
        userEntity.setSenhaHash(requestUser.senhaHash());

        UserEntity savedUserEntity = userEntityRepository.save(userEntity);

        return new UserResponse(
                savedUserEntity.getId(),
                savedUserEntity.getName(),
                savedUserEntity.getEmail()
        );
    }

    //DELETAR  USUARIO
    public void deletarUsuario(Long id) {
        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        userEntityRepository.delete(userEntity);
    }

    //LISTAR USUARIOS
    public List<UserResponse> listarUsuarios() {

        List<UserEntity> usuarios = userEntityRepository.findAll();

        return usuarios.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    //Login
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        return new LoginResponse("Login successful");
    }

}