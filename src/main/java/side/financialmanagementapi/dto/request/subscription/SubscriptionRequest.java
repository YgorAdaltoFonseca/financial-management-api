package side.financialmanagementapi.dto.request.subscription;

import side.financialmanagementapi.enums.subscription.FrequencyEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionRequest (
        String name,
        BigDecimal value,
        FrequencyEnum frequency,
        LocalDate startDate
){}
