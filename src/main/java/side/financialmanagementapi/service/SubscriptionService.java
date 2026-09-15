package side.financialmanagementapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.request.SubscriptionRequest;
import side.financialmanagementapi.dto.response.SubscriptionResponse;
import side.financialmanagementapi.entities.CategoryTypeEntity;
import side.financialmanagementapi.entities.SubscriptionEntity;
import side.financialmanagementapi.entities.UserEntity;
import side.financialmanagementapi.exceptions.CategoryNotFoundException;
import side.financialmanagementapi.exceptions.SubscriptionNotFoundException;
import side.financialmanagementapi.exceptions.UserNotFoundException;
import side.financialmanagementapi.repository.CategoryTypeRepository;
import side.financialmanagementapi.repository.SubscriptionRepository;
import side.financialmanagementapi.repository.UserEntityRepository;
import java.time.LocalDate;
import java.util.List;

import static side.financialmanagementapi.enums.SubscriptionStatusEnum.ACTIVE;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final UserEntityRepository userEntityRepository;

    //Metado de proxima assinatura
    public LocalDate getNextCharge(SubscriptionRequest request) {

        return switch (request.frequency()) {

            case WEEKLY -> request.startDate().plusWeeks(1);

            case MONTHLY -> request.startDate().plusMonths(1);

            case QUARTERLY -> request.startDate().plusMonths(3);

            case SEMIANNUAL -> request.startDate().plusMonths(6);

            case ANNUAL -> request.startDate().plusYears(1);
        };
    }

    //Criar assinatura
    public SubscriptionResponse createSubscription(
            SubscriptionRequest subscriptionRequest,
            Long userId,
            Long categoryId
    ) {
        UserEntity userEntity = userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CategoryTypeEntity categoryType = categoryTypeRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        LocalDate nextCharge = getNextCharge(subscriptionRequest);

        SubscriptionEntity subscriptionEntity = SubscriptionEntity.builder()
                .name(subscriptionRequest.name())
                .value(subscriptionRequest.value())
                .frequency(subscriptionRequest.frequency())
                .startDate(subscriptionRequest.startDate())
                .nextCharge(nextCharge)
                .subscriptionStatus(ACTIVE)
                .user(userEntity)
                .categoryType(categoryType)
                .build();

        SubscriptionEntity subscriptionSaved = subscriptionRepository.save(subscriptionEntity);

        return new SubscriptionResponse(
                subscriptionSaved.getId(),
                subscriptionSaved.getName(),
                subscriptionSaved.getValue(),
                subscriptionSaved.getFrequency(),
                subscriptionSaved.getStartDate(),
                subscriptionSaved.getNextCharge(),
                subscriptionSaved.getSubscriptionStatus(),
                subscriptionSaved.getUser().getId(),
                subscriptionSaved.getUser().getName(),
                subscriptionSaved.getCategoryType().getId(),
                subscriptionSaved.getCategoryType().getName()
        );
    }

    //Atualizar
    public SubscriptionResponse updateSubscription(
            Long id ,
            Long userId ,
            Long categoryId ,
            SubscriptionRequest subscriptionRequest
    ) {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        UserEntity userEntity = userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CategoryTypeEntity categoryType = categoryTypeRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        LocalDate nextCharge = getNextCharge(subscriptionRequest);

        subscriptionEntity.setName(subscriptionRequest.name());
        subscriptionEntity.setValue(subscriptionRequest.value());
        subscriptionEntity.setFrequency(subscriptionRequest.frequency());
        subscriptionEntity.setStartDate(subscriptionRequest.startDate());
        subscriptionEntity.setNextCharge(nextCharge);
        subscriptionEntity.setUser(userEntity);
        subscriptionEntity.setCategoryType(categoryType);

        SubscriptionEntity subscriptionSaved = subscriptionRepository.save(subscriptionEntity);

        return new SubscriptionResponse(
                subscriptionSaved.getId(),
                subscriptionSaved.getName(),
                subscriptionSaved.getValue(),
                subscriptionSaved.getFrequency(),
                subscriptionSaved.getStartDate(),
                subscriptionSaved.getNextCharge(),
                subscriptionSaved.getSubscriptionStatus(),
                subscriptionSaved.getUser().getId(),
                subscriptionSaved.getUser().getName(),
                subscriptionSaved.getCategoryType().getId(),
                subscriptionSaved.getCategoryType().getName()

        );
    }

    //Excluir
    public void deleteSubscription(Long id) {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        subscriptionRepository.delete(subscriptionEntity);
    }

    //Listar Assinaturas
    public List<SubscriptionResponse> listSubscriptions() {
        List<SubscriptionEntity> subscriptionEntities =  subscriptionRepository.findAll();

        return subscriptionEntities
                .stream()
                .map(subscriptionEntity ->
                        new SubscriptionResponse(
                                subscriptionEntity.getId() ,
                                subscriptionEntity.getName() ,
                                subscriptionEntity.getValue() ,
                                subscriptionEntity.getFrequency() ,
                                subscriptionEntity.getStartDate() ,
                                subscriptionEntity.getNextCharge() ,
                                subscriptionEntity.getSubscriptionStatus() ,
                                subscriptionEntity.getUser().getId() ,
                                subscriptionEntity.getUser().getName() ,
                                subscriptionEntity.getCategoryType().getId() ,
                                subscriptionEntity.getCategoryType().getName()
                        )
                ).toList();
    }

}
