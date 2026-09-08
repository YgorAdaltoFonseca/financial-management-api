package side.financialmanagementapi.dto.request;

import side.financialmanagementapi.enums.SubscriptionStatusEnum;
import side.financialmanagementapi.enums.TransactionOriginEnum;
import side.financialmanagementapi.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(
        BigDecimal value,
        TransactionTypeEnum type,
        TransactionOriginEnum origin,
        String description ,
        SubscriptionStatusEnum subscriptionStatus
) {}
