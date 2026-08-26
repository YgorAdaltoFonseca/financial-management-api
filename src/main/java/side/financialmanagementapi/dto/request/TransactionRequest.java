package side.financialmanagementapi.dto.request;

import side.financialmanagementapi.enums.TransactionOriginEnum;
import side.financialmanagementapi.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(
        BigDecimal value,
        LocalDateTime dateTime,
        TransactionTypeEnum type,
        TransactionOriginEnum origin,
        String description,
        Long categoryTypeId
) {}
