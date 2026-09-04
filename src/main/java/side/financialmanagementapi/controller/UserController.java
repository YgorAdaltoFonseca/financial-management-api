package side.financialmanagementapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import side.financialmanagementapi.dto.request.UserRequest;
import side.financialmanagementapi.repository.UserEntityRepository;
import side.financialmanagementapi.service.UserService;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;


    @PostMapping(name="/createUser")
    public ResponseEntity createUser(@RequestBody UserRequest userRequest) {
        userService.cadastroUsuario(userRequest);
        return ResponseEntity.ok().build();
    }

}
