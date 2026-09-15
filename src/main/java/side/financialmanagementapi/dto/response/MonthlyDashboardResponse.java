package side.financialmanagementapi.dto.response;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlyDashboardResponse(
        YearMonth month,
        BigDecimal entries,
        BigDecimal exit
) {
}
