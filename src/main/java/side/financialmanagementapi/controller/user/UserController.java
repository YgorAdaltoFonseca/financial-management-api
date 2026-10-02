package side.financialmanagementapi.controller.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.user.LoginRequest;
import side.financialmanagementapi.dto.request.user.UserRequest;
import side.financialmanagementapi.dto.response.user.LoginResponse;
import side.financialmanagementapi.dto.response.user.UserResponse;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.repository.user.UserEntityRepository;
import side.financialmanagementapi.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;
    public final UserEntityRepository userEntityRepository;


    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        userService.cadastroUsuario(userRequest);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "bearer-key")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {

        userService.atualizarUsuario(id, userRequest);

        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deletarUsuario(id);

        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/usersList")
    public ResponseEntity<List<UserResponse>> listarUsuarios() {

        List<UserResponse> usuarios = userService.listarUsuarios();

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {
        return userService.login(request);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Optional<UserEntity> user =
                userEntityRepository.findByEmail(authentication.getName());

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserEntity userEntity = user.get();

        return ResponseEntity.ok(
                new UserResponse(
                        userEntity.getId(),
                        userEntity.getName(),
                        userEntity.getEmail()
                )
        );
    }

}
