
package side.financialmanagementapi.controller.subscription;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.subscription.SubscriptionRequest;
import side.financialmanagementapi.dto.response.subscription.SubscriptionResponse;
import side.financialmanagementapi.service.subscription.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/createSubscription/{categoryId}")
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody SubscriptionRequest request,
            @PathVariable Long categoryId
    ) {
        SubscriptionResponse response =
                subscriptionService.createSubscription(request, categoryId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateSubscription/{id}/{categoryId}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable Long id,
            @PathVariable Long categoryId,
            @Valid @RequestBody SubscriptionRequest request
    ) {
        SubscriptionResponse response =
                subscriptionService.updateSubscription(id, categoryId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteSubscription/{id}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable Long id
    ) {
        subscriptionService.deleteSubscription(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listSubscriptions")
    public ResponseEntity<List<SubscriptionResponse>> listSubscriptions() {
        return ResponseEntity.ok(
                subscriptionService.listSubscriptions()
        );
    }
}