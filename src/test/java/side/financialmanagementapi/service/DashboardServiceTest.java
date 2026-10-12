package side.financialmanagementapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.repository.subscription.SubscriptionRepository;
import side.financialmanagementapi.repository.transaction.TransactionRepository;
import side.financialmanagementapi.service.user.AuthenticatedUserService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {
    @Mock TransactionRepository transactionRepository;
    @Mock SubscriptionRepository subscriptionRepository;
    @Mock AuthenticatedUserService authenticatedUserService;
    @InjectMocks DashboardService service;

    @Test
    void totalsAreRestrictedToAuthenticatedUser() {
        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(UserEntity.builder().id(42L).build());
        when(transactionRepository.findAllByUser_Id(42L)).thenReturn(List.of());
        when(subscriptionRepository.findAllByUser_Id(42L)).thenReturn(List.of());

        assertEquals(BigDecimal.ZERO, service.totalEntries());
        assertEquals(BigDecimal.ZERO, service.totalExit());
        assertEquals(BigDecimal.ZERO, service.totalSubscription());
        verify(transactionRepository, times(2)).findAllByUser_Id(42L);
        verify(subscriptionRepository).findAllByUser_Id(42L);
        verify(transactionRepository, never()).findAll();
        verify(subscriptionRepository, never()).findAll();
    }
}
