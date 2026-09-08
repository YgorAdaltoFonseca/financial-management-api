package side.financialmanagementapi.controller;

import jdk.jshell.JShell;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.SubscriptionRequest;
import side.financialmanagementapi.dto.response.SubscriptionResponse;
import side.financialmanagementapi.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/createSubscription/{userId}/{categoryId}")
    public ResponseEntity<SubscriptionResponse>  createSubscription(
            @RequestBody SubscriptionRequest subscriptionRequest ,
            @PathVariable Long userId ,
            @PathVariable Long categoryId
    ) {
        SubscriptionResponse subscriptionResponse =
                subscriptionService.createSubscription(subscriptionRequest , userId , categoryId);

        return ResponseEntity.ok(subscriptionResponse);
    }

    @PutMapping("/updateSubscription/{id}/{userId}/{categoryId}")
    public ResponseEntity<SubscriptionResponse>  updateSubscription(
            @PathVariable Long id,
            @PathVariable Long userId ,
            @PathVariable Long categoryId ,
            @RequestBody SubscriptionRequest subscriptionRequest
    ) {
        SubscriptionResponse subscriptionResponse =
                subscriptionService.updateSubscription
                        (id ,  userId , categoryId , subscriptionRequest);

        return ResponseEntity.ok(subscriptionResponse);
    }

    @DeleteMapping("/deleteSubscription/{id}")
    public ResponseEntity<Void>  deleteSubscription(
            @PathVariable Long id
    ){
        subscriptionService.deleteSubscription(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/listSubscriptions")
    public ResponseEntity<List<SubscriptionResponse>>  listSubscriptions(){

        List<SubscriptionResponse> subscriptionResponse =
                subscriptionService.listSubscriptions();

        return ResponseEntity.ok(subscriptionResponse);
    }

}
