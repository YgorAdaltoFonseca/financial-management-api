package side.financialmanagementapi.dto.request.user;

public record LoginRequest(
        String email,
        String senha
) {
}
