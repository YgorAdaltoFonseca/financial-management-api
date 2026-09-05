package side.financialmanagementapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.UserRequest;
import side.financialmanagementapi.dto.response.UserResponse;
import side.financialmanagementapi.repository.UserEntityRepository;
import side.financialmanagementapi.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;


    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        userService.cadastroUsuario(userRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {

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

}
