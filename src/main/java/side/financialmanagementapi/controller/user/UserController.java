
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
import side.financialmanagementapi.service.user.AuthenticatedUserService;
import side.financialmanagementapi.service.user.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(
                userService.cadastroUsuario(request)
        );
    }

    @SecurityRequirement(name = "bearer-key")
    @PutMapping("/updateUser")
    public ResponseEntity<UserResponse> updateUser(
            @Valid @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(
                userService.atualizarUsuario(request)
        );
    }

    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser() {
        userService.deletarUsuario();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                userService.login(request)
        );
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {

        UserEntity user =
                authenticatedUserService.getAuthenticatedUser();

        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }
}