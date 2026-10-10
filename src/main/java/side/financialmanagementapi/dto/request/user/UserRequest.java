package side.financialmanagementapi.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank
        @Size(max = 100)
        String name ,
        @NotBlank
        @Email
        String email ,
        @NotBlank
        @Size(min = 8, max = 72)
        String senhaHash
) {}
