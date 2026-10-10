package side.financialmanagementapi.dto.request.transaction;

import side.financialmanagementapi.enums.subscription.SubscriptionStatusEnum;
import side.financialmanagementapi.enums.transaction.TransactionOriginEnum;
import side.financialmanagementapi.enums.transaction.TransactionTypeEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull @DecimalMin(value = "0.01") BigDecimal value,
        @NotNull TransactionTypeEnum type,
        @NotNull TransactionOriginEnum origin,
        @NotBlank @Size(max = 255) String description ,
        SubscriptionStatusEnum subscriptionStatus
) {}
