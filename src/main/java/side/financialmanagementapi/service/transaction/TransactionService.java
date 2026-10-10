package side.financialmanagementapi.service.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.request.transaction.TransactionRequest;
import side.financialmanagementapi.dto.response.transaction.TransactionResponse;
import side.financialmanagementapi.entities.category.CategoryTypeEntity;
import side.financialmanagementapi.entities.transaction.TransactionEntity;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.exceptions.CategoryNotFoundException;
import side.financialmanagementapi.exceptions.TransactionNotFoundException;
import side.financialmanagementapi.repository.category.CategoryTypeRepository;
import side.financialmanagementapi.repository.transaction.TransactionRepository;
import side.financialmanagementapi.service.user.AuthenticatedUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public TransactionResponse createTransaction(TransactionRequest request, Long categoryId) {

        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        CategoryTypeEntity category = categoryTypeRepository.findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        TransactionEntity transaction = TransactionEntity.builder()
                .value(request.value()).type(request.type()).origin(request.origin())
                .description(request.description()).categoryType(category).user(user).build();

        return toResponse(transactionRepository.save(transaction));
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request, Long categoryId) {

        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        TransactionEntity transaction = transactionRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        CategoryTypeEntity category = categoryTypeRepository.findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        transaction.setValue(request.value());
        transaction.setType(request.type());
        transaction.setOrigin(request.origin());
        transaction.setDescription(request.description());
        transaction.setCategoryType(category);

        return toResponse(transactionRepository.save(transaction));
    }

    public void deleteTransaction(Long id) {

        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        TransactionEntity transaction = transactionRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        transactionRepository.delete(transaction);
    }

    public List<TransactionResponse> listTransactions() {

        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        return transactionRepository.findAllByUser_Id(user.getId())
                .stream().map(this::toResponse).toList();
    }

    private TransactionResponse toResponse(TransactionEntity transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getValue(),
                transaction.getDateTime(),
                transaction.getType(),
                transaction.getOrigin(),
                transaction.getDescription(),
                transaction.getCategoryType().getId(),
                transaction.getCategoryType().getName(),
                transaction.getUser().getId(),
                transaction.getUser().getName());
    }
}
