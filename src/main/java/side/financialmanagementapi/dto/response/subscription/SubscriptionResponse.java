package side.financialmanagementapi.dto.response.subscription;

import side.financialmanagementapi.enums.subscription.FrequencyEnum;
import side.financialmanagementapi.enums.subscription.SubscriptionStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponse(
        Long id,
        String name,
        BigDecimal value,
        FrequencyEnum frequency,
        LocalDate startDate,
        LocalDate nextCharge,
        SubscriptionStatusEnum subscriptionStatus,
        Long userId ,
        String userName ,
        Long categoryTypeId,
        String categoryTypeName



) {}
