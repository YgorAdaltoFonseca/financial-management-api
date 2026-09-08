package side.financialmanagementapi.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.financialmanagementapi.dto.request.TransactionRequest;
import side.financialmanagementapi.dto.response.TransactionResponse;
import side.financialmanagementapi.repository.TransactionRepository;
import side.financialmanagementapi.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/createTransaction/{CategoryTypeId}/{UserId}")
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestBody TransactionRequest transactionRequest,
            @PathVariable Long CategoryTypeId,
            @PathVariable Long UserId
    ) {
        TransactionResponse response =
        transactionService.createTransaction(transactionRequest, CategoryTypeId, UserId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("updateTransaction/{id}/{UserId}/{CategoryTypeId}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id ,
            @RequestBody TransactionRequest request ,
            @PathVariable Long CategoryTypeId ,
            @PathVariable Long UserId
    ){
        TransactionResponse response =
        transactionService.updateTransaction(id, request, CategoryTypeId ,UserId  );

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
