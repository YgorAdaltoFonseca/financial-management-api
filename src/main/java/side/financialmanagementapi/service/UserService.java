package side.financialmanagementapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.request.UserRequest;
import side.financialmanagementapi.dto.response.UserResponse;
import side.financialmanagementapi.entities.UserEntity;
import side.financialmanagementapi.repository.UserEntityRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userEntityRepository;

    //CADASTRAR USUARIO
    public UserResponse cadastroUsuario(UserRequest requestUser) {
        if (userEntityRepository.existsByEmail(requestUser.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        UserEntity userEntity = UserEntity.builder()
                .name(requestUser.name())
                .email(requestUser.email())
                .senhaHash(requestUser.senhaHash())
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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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


}