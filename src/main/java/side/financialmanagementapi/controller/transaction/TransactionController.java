package side.financialmanagementapi.controller.transaction;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.transaction.TransactionRequest;
import side.financialmanagementapi.dto.response.transaction.TransactionResponse;
import side.financialmanagementapi.service.transaction.TransactionService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/transaction")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/createTransaction/{CategoryTypeId}")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest,
            @PathVariable Long CategoryTypeId

    ) {
        TransactionResponse response =
        transactionService.createTransaction(transactionRequest, CategoryTypeId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("updateTransaction/{id}/{CategoryTypeId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id ,
            @Valid @RequestBody TransactionRequest request ,
            @PathVariable Long CategoryTypeId
    ){
        TransactionResponse response =
        transactionService.updateTransaction(id, request, CategoryTypeId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id
    ){
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listTransaction")
    public ResponseEntity<List<TransactionResponse>> listTransactions(){
        List<TransactionResponse> transactionEntities = transactionService.listTransactions();
        return ResponseEntity.ok(transactionEntities);
    }

}
