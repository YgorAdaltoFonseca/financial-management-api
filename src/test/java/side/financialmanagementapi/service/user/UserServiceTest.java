package side.financialmanagementapi.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import side.financialmanagementapi.dto.request.user.UserRequest;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.exceptions.EmailAlreadyExistsException;
import side.financialmanagementapi.repository.user.UserEntityRepository;
import side.financialmanagementapi.service.JwtService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserEntityRepository users;
    @Mock PasswordEncoder passwordEncoder;
    @Mock AuthenticationManager authenticationManager;
    @Mock JwtService jwtService;
    @Mock AuthenticatedUserService authenticatedUserService;
    @InjectMocks UserService service;

    @Test
    void registrationStoresAnEncodedPasswordAndOmitsItFromResponse() {
        when(users.existsByEmail("ana@example.test")).thenReturn(false);
        when(passwordEncoder.encode("secure-password")).thenReturn("$2a$encoded-value");
        when(users.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity saved = invocation.getArgument(0);
            saved.setId(5L);
            return saved;
        });

        var response = service.cadastroUsuario(new UserRequest("Ana", "ana@example.test", "secure-password"));

        assertEquals("ana@example.test", response.email());
        assertEquals("Ana", response.name());
        verify(passwordEncoder).encode("secure-password");
        verify(users).save(argThat(user -> "$2a$encoded-value".equals(user.getPassword())));
        assertFalse(response.toString().contains("encoded-value"));
    }

    @Test
    void duplicateEmailIsRejectedBeforePasswordEncoding() {
        when(users.existsByEmail("ana@example.test")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () ->
                service.cadastroUsuario(new UserRequest("Ana", "ana@example.test", "secure-password")));

        verify(passwordEncoder, never()).encode(any());
        verify(users, never()).save(any());
    }
}
