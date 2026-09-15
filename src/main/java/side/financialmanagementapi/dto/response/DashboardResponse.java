package side.financialmanagementapi.dto.response;

import java.math.BigDecimal;

public record DashboardResponse(
        BigDecimal totalEntries,
        BigDecimal totalExit,
        BigDecimal totalSubscriptions
) {
}
