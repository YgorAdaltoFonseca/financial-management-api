package side.financialmanagementapi.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.response.DashboardResponse;
import side.financialmanagementapi.dto.response.MonthlyDashboardResponse;
import side.financialmanagementapi.entities.subscription.SubscriptionEntity;
import side.financialmanagementapi.entities.transaction.TransactionEntity;
import side.financialmanagementapi.enums.subscription.SubscriptionStatusEnum;
import side.financialmanagementapi.enums.transaction.TransactionTypeEnum;
import side.financialmanagementapi.repository.subscription.SubscriptionRepository;
import side.financialmanagementapi.repository.transaction.TransactionRepository;
import side.financialmanagementapi.service.user.AuthenticatedUserService;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
@AllArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AuthenticatedUserService authenticatedUserService;

    private Long currentUserId() {
        return authenticatedUserService.getAuthenticatedUser().getId();
    }

    public BigDecimal totalEntries() {
        return transactionRepository.findAllByUser_Id(currentUserId())
                .stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.ENTRY)
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalExit() {
        return transactionRepository.findAllByUser_Id(currentUserId())
                .stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.EXIT)
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal totalSubscription() {
        return subscriptionRepository.findAllByUser_Id(currentUserId())
                .stream()
                .filter(subscription -> subscription.getSubscriptionStatus() == SubscriptionStatusEnum.ACTIVE)
                .map(SubscriptionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ResponseEntity<DashboardResponse> dashboard() {
        return ResponseEntity.
                ok(new DashboardResponse(
                        totalEntries(),
                        totalExit(),
                        totalSubscription()));
    }

    public List<MonthlyDashboardResponse> monthlyDashboard() {

        List<TransactionEntity> transactions = transactionRepository.findAllByUser_Id(currentUserId());

        List<YearMonth> months = transactions.stream()
                .map(transaction -> YearMonth.from(transaction.getDateTime()))
                .distinct()
                .sorted()
                .toList();

        return months.stream()
                .map(month -> {

                    BigDecimal entries = transactions.stream()
                            .filter(transaction ->
                                    YearMonth.from(transaction.getDateTime()).equals(month))
                            .filter(transaction ->
                                    transaction.getType() == TransactionTypeEnum.ENTRY)
                            .map(TransactionEntity::getValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal exit = transactions.stream()
                            .filter(transaction ->
                                    YearMonth.from(transaction.getDateTime()).equals(month))
                            .filter(transaction ->
                                    transaction.getType() == TransactionTypeEnum.EXIT)
                            .map(TransactionEntity::getValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new MonthlyDashboardResponse(
                            month,
                            entries,
                            exit
                    );
                })
                .toList();
    }

}
