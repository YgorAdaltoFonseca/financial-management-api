package side.financialmanagementapi.dto.request;

public record UserRequest(
        String name ,
        String email ,
        String senhaHash
) {}
