package side.financialmanagementapi.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long id ,
        String name ,
        String email ,
        String senhaHash ,
        LocalDateTime registrationDate
) {}
