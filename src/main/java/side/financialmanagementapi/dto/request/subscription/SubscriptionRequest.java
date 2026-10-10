package side.financialmanagementapi.dto.request.subscription;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import side.financialmanagementapi.enums.subscription.FrequencyEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionRequest (
        @NotBlank @Size(max = 100) String name,
        @NotNull @DecimalMin(value = "0.01") BigDecimal value,
        @NotNull FrequencyEnum frequency,
        @NotNull LocalDate startDate
){}
