package side.financialmanagementapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.request.TransactionRequest;
import side.financialmanagementapi.dto.response.TransactionResponse;
import side.financialmanagementapi.entities.CategoryTypeEntity;
import side.financialmanagementapi.entities.TransactionEntity;
import side.financialmanagementapi.entities.UserEntity;
import side.financialmanagementapi.repository.CategoryTypeRepository;
import side.financialmanagementapi.repository.TransactionRepository;
import side.financialmanagementapi.repository.UserEntityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
        private final TransactionRepository transactionRepository;
        private final UserEntityRepository userEntityRepository;
        private final CategoryTypeRepository categoryTypeRepository;

        public TransactionResponse createTransaction(
                TransactionRequest transactionRequest ,
                Long categoryTypeId ,
                Long userId
        ) {
            System.out.println("categoryTypeId recebido: " + categoryTypeId);

            CategoryTypeEntity categoryType = categoryTypeRepository.findById(categoryTypeId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            UserEntity user = userEntityRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            TransactionEntity transactionEntity = TransactionEntity.builder()
                    .value(transactionRequest.value())
                    .type(transactionRequest.type())
                    .origin(transactionRequest.origin())
                    .description(transactionRequest.description())
                    .categoryType(categoryType)
                    .user(user)
                    .build();

           TransactionEntity transactionEntitySaved = transactionRepository.save(transactionEntity);


            return new TransactionResponse(
                    transactionEntitySaved.getId(),
                    transactionEntitySaved.getValue(),
                    transactionEntitySaved.getDateTime(),
                    transactionEntitySaved.getType(),
                    transactionEntitySaved.getOrigin(),
                    transactionEntitySaved.getDescription(),
                    transactionEntitySaved.getCategoryType().getId() ,
                    transactionEntitySaved.getCategoryType().getName() ,
                    transactionEntitySaved.getUser().getId() ,
                    transactionEntitySaved.getUser().getName()

            );
        }

        //Atualizar transacao
        public TransactionResponse updateTransaction(
                Long id ,
                TransactionRequest transactionRequest ,
                Long categoryTypeId ,
                Long userId
        ) {
            TransactionEntity transactionEntity = transactionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

            CategoryTypeEntity categoryType = categoryTypeRepository.findById(categoryTypeId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            UserEntity user = userEntityRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

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
                    transactionEntitySaved.getCategoryType().getId() ,
                    transactionEntitySaved.getCategoryType().getName() ,
                    transactionEntitySaved.getUser().getId() ,
                    transactionEntitySaved.getUser().getName()
            );
        }

        //deletar
        public void deleteTransaction(Long id) {
            TransactionEntity transactionEntity = transactionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));

            transactionRepository.delete(transactionEntity);
        }

        //listaqr transaction
        public List<TransactionResponse> listTransactions() {
            List<TransactionEntity> transactionEntities = transactionRepository.findAll();

            return transactionEntities.stream()
                    .map(transactionEntity -> new TransactionResponse(
                            transactionEntity.getId() ,
                            transactionEntity.getValue() ,
                            transactionEntity.getDateTime() ,
                            transactionEntity.getType() ,
                            transactionEntity.getOrigin(),
                            transactionEntity.getDescription() ,
                            transactionEntity.getCategoryType().getId() ,
                            transactionEntity.getCategoryType().getName() ,
                            transactionEntity.getUser().getId() ,
                            transactionEntity.getUser().getName()
                    )).toList();
        }
}
