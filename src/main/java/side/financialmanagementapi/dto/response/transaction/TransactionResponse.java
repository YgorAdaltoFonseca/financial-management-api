package side.financialmanagementapi.dto.response.transaction;

import side.financialmanagementapi.enums.transaction.TransactionOriginEnum;
import side.financialmanagementapi.enums.transaction.TransactionTypeEnum;

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
        String categoryTypeName ,
        Long userId ,
        String userName
){}
