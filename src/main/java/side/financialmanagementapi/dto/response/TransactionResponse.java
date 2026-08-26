package side.financialmanagementapi.dto.response;

import side.financialmanagementapi.enums.TransactionOriginEnum;
import side.financialmanagementapi.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        BigDecimal value,
        LocalDateTime dateTime,
        TransactionTypeEnum type,
        TransactionOriginEnum origin,
        String description,
        Long categoryTypeId,
        String categoryTypeName
){}
