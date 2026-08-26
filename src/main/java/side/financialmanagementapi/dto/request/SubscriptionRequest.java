package side.financialmanagementapi.dto.request;

import side.financialmanagementapi.enums.FrequencyEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionRequest (
        String name,
        BigDecimal value,
        FrequencyEnum frequency,
        LocalDate startDate,
        Long categoryTypeId
){}
