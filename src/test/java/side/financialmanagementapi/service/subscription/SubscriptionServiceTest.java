package side.financialmanagementapi.service.subscription;

import org.junit.jupiter.api.Test;
import side.financialmanagementapi.dto.request.subscription.SubscriptionRequest;
import side.financialmanagementapi.enums.subscription.FrequencyEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionServiceTest {
    private final SubscriptionService service = new SubscriptionService(null, null, null);

    @Test
    void calculatesSupportedNextChargeFrequencies() {
        LocalDate start = LocalDate.of(2025, 1, 31);
        assertEquals(LocalDate.of(2025, 2, 28), service.getNextCharge(request(FrequencyEnum.MONTHLY, start)));
        assertEquals(LocalDate.of(2025, 4, 30), service.getNextCharge(request(FrequencyEnum.QUARTERLY, start)));
        assertEquals(LocalDate.of(2026, 1, 31), service.getNextCharge(request(FrequencyEnum.ANNUAL, start)));
    }

    private SubscriptionRequest request(FrequencyEnum frequency, LocalDate start) {
        return new SubscriptionRequest("Sample", BigDecimal.TEN, frequency, start);
    }
}
