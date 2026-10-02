package side.financialmanagementapi.dto.request.transaction;

import side.financialmanagementapi.enums.subscription.SubscriptionStatusEnum;
import side.financialmanagementapi.enums.transaction.TransactionOriginEnum;
import side.financialmanagementapi.enums.transaction.TransactionTypeEnum;

import java.math.BigDecimal;

public record TransactionRequest(
        BigDecimal value,
        TransactionTypeEnum type,
        TransactionOriginEnum origin,
        String description ,
        SubscriptionStatusEnum subscriptionStatus
) {}
