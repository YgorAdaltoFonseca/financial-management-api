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
import side.financialmanagementapi.exceptions.UserNotFoundException;
import side.financialmanagementapi.repository.category.CategoryTypeRepository;
import side.financialmanagementapi.repository.transaction.TransactionRepository;
import side.financialmanagementapi.repository.user.UserEntityRepository;
import side.financialmanagementapi.service.user.AuthenticatedUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserEntityRepository userEntityRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public TransactionResponse createTransaction(
            TransactionRequest transactionRequest,
            Long categoryTypeId
    ) {

        CategoryTypeEntity categoryType =
                categoryTypeRepository.findById(categoryTypeId)
                        .orElseThrow(() ->
                                new CategoryNotFoundException("Category not found"));

        UserEntity user =
                authenticatedUserService.getAuthenticatedUser();

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .value(transactionRequest.value())
                .type(transactionRequest.type())
                .origin(transactionRequest.origin())
                .description(transactionRequest.description())
                .categoryType(categoryType)
                .user(user)
                .build();

        TransactionEntity transactionEntitySaved =
                transactionRepository.save(transactionEntity);

        return new TransactionResponse(
                transactionEntitySaved.getId(),
                transactionEntitySaved.getValue(),
                transactionEntitySaved.getDateTime(),
                transactionEntitySaved.getType(),
                transactionEntitySaved.getOrigin(),
                transactionEntitySaved.getDescription(),
                transactionEntitySaved.getCategoryType().getId(),
                transactionEntitySaved.getCategoryType().getName(),
                transactionEntitySaved.getUser().getId(),
                transactionEntitySaved.getUser().getName()
        );
    }

    //Atualizar transacao
    public TransactionResponse updateTransaction(
            Long id,
            TransactionRequest transactionRequest,
            Long categoryTypeId
    ) {
        TransactionEntity transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        CategoryTypeEntity categoryType = categoryTypeRepository.findById(categoryTypeId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        UserEntity user =
                authenticatedUserService.getAuthenticatedUser();

        transactionEntity.setValue(transactionRequest.value());
        transactionEntity.setType(transactionRequest.type());
        transactionEntity.setOrigin(transactionRequest.origin());
        transactionEntity.setDescription(transactionRequest.description());
        transactionEntity.setCategoryType(categoryType);
        transactionEntity.setUser(user);

        TransactionEntity transactionEntitySaved = transactionRepository.save(transactionEntity);
        return new TransactionResponse(
                transactionEntitySaved.getId(),
                transactionEntitySaved.getValue(),
                transactionEntitySaved.getDateTime(),
                transactionEntitySaved.getType(),
                transactionEntitySaved.getOrigin(),
                transactionEntitySaved.getDescription(),
                transactionEntitySaved.getCategoryType().getId(),
                transactionEntitySaved.getCategoryType().getName(),
                transactionEntitySaved.getUser().getId(),
                transactionEntitySaved.getUser().getName()
        );
    }

    //deletar
    public void deleteTransaction(Long id) {

        UserEntity user =
                authenticatedUserService.getAuthenticatedUser();

        TransactionEntity transactionEntity =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new TransactionNotFoundException("Transaction not found"));

        if (!transactionEntity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Transaction does not belong to authenticated user");
        }

        transactionRepository.delete(transactionEntity);
    }

    //listaqr transaction
    public List<TransactionResponse> listTransactions() {

        UserEntity user = authenticatedUserService.getAuthenticatedUser();


        List<TransactionEntity> transactionEntities =
                transactionRepository.findAllByUserId(user.getId());

        return transactionEntities.stream()
                .map(transactionEntity -> new TransactionResponse(
                        transactionEntity.getId(),
                        transactionEntity.getValue(),
                        transactionEntity.getDateTime(),
                        transactionEntity.getType(),
                        transactionEntity.getOrigin(),
                        transactionEntity.getDescription(),
                        transactionEntity.getCategoryType().getId(),
                        transactionEntity.getCategoryType().getName(),
                        transactionEntity.getUser().getId(),
                        transactionEntity.getUser().getName()
                )).toList();
    }
}
