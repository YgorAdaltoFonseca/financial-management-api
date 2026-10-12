package side.financialmanagementapi.service.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import side.financialmanagementapi.entities.transaction.TransactionEntity;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.exceptions.TransactionNotFoundException;
import side.financialmanagementapi.repository.category.CategoryTypeRepository;
import side.financialmanagementapi.repository.transaction.TransactionRepository;
import side.financialmanagementapi.service.user.AuthenticatedUserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock TransactionRepository transactionRepository;
    @Mock CategoryTypeRepository categoryTypeRepository;
    @Mock AuthenticatedUserService authenticatedUserService;
    @InjectMocks TransactionService service;

    @Test
    void deleteOnlyLooksUpTransactionOwnedByAuthenticatedUser() {
        UserEntity user = UserEntity.builder().id(8L).name("A").build();
        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(user);
        when(transactionRepository.findByIdAndUser_Id(21L, 8L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> service.deleteTransaction(21L));

        verify(transactionRepository, never()).delete(any(TransactionEntity.class));
    }

    @Test
    void updateDoesNotLoadTransactionsByUnscopedId() {
        UserEntity user = UserEntity.builder().id(8L).name("A").build();
        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(user);
        when(transactionRepository.findByIdAndUser_Id(21L, 8L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class,
                () -> service.updateTransaction(21L, null, 2L));

        verify(transactionRepository, never()).findById(21L);
        verify(transactionRepository, never()).save(any());
    }
}
