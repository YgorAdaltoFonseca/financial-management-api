package side.financialmanagementapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.UserRequest;
import side.financialmanagementapi.repository.UserEntityRepository;
import side.financialmanagementapi.service.UserService;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;


    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody UserRequest userRequest) {
        userService.cadastroUsuario(userRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {

        userService.atualizarUsuario(id, userRequest);

        return ResponseEntity.ok().build();
    }

}
