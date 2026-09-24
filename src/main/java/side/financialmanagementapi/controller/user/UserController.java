package side.financialmanagementapi.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.user.LoginRequest;
import side.financialmanagementapi.dto.request.user.UserRequest;
import side.financialmanagementapi.dto.response.user.LoginResponse;
import side.financialmanagementapi.dto.response.user.UserResponse;
import side.financialmanagementapi.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;


    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        userService.cadastroUsuario(userRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {

        userService.atualizarUsuario(id, userRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deletarUsuario(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usersList")
    public ResponseEntity<List<UserResponse>> listarUsuarios() {

        List<UserResponse> usuarios = userService.listarUsuarios();

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ){
        return userService.login(request);
    }

}
