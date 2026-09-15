package side.financialmanagementapi.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.response.DashboardResponse;
import side.financialmanagementapi.dto.response.MonthlyDashboardResponse;
import side.financialmanagementapi.entities.SubscriptionEntity;
import side.financialmanagementapi.entities.TransactionEntity;
import side.financialmanagementapi.enums.SubscriptionStatusEnum;
import side.financialmanagementapi.enums.TransactionTypeEnum;
import side.financialmanagementapi.repository.SubscriptionRepository;
import side.financialmanagementapi.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
@AllArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final SubscriptionRepository subscriptionRepository;

    public BigDecimal totalEntries() {
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.ENTRY)
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalExit() {
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.EXIT)
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal totalSubscription() {
        return subscriptionRepository.findAll()
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

        List<TransactionEntity> transactions = transactionRepository.findAll();

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
