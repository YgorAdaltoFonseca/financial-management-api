package side.financialmanagementapi.dto.response;

import side.financialmanagementapi.enums.FrequencyEnum;
import side.financialmanagementapi.enums.SubscriptionStatusEnum;

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
        Long categoryTypeId,
        String categoryTypeName
) {}
