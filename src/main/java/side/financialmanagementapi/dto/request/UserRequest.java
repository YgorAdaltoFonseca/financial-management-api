package side.financialmanagementapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        String name ,
        @NotBlank
        @Email
        String email ,
        String senhaHash
) {}
