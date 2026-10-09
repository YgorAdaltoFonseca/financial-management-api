
package side.financialmanagementapi.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import side.financialmanagementapi.dto.request.subscription.SubscriptionRequest;
import side.financialmanagementapi.dto.response.subscription.SubscriptionResponse;
import side.financialmanagementapi.entities.category.CategoryTypeEntity;
import side.financialmanagementapi.entities.subscription.SubscriptionEntity;
import side.financialmanagementapi.entities.user.UserEntity;
import side.financialmanagementapi.exceptions.CategoryNotFoundException;
import side.financialmanagementapi.exceptions.SubscriptionNotFoundException;
import side.financialmanagementapi.repository.category.CategoryTypeRepository;
import side.financialmanagementapi.repository.subscription.SubscriptionRepository;
import side.financialmanagementapi.service.user.AuthenticatedUserService;

import java.time.LocalDate;
import java.util.List;

import static side.financialmanagementapi.enums.subscription.SubscriptionStatusEnum.ACTIVE;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final AuthenticatedUserService authenticatedUserService;

    // Calcula a proxima cobrança
    public LocalDate getNextCharge(SubscriptionRequest request) {
        return switch (request.frequency()) {
            case WEEKLY -> request.startDate().plusWeeks(1);
            case MONTHLY -> request.startDate().plusMonths(1);
            case QUARTERLY -> request.startDate().plusMonths(3);
            case SEMIANNUAL -> request.startDate().plusMonths(6);
            case ANNUAL -> request.startDate().plusYears(1);
        };
    }

    // Cria uma assinatura para o usuario autenticado
    public SubscriptionResponse createSubscription(
            SubscriptionRequest request,
            Long categoryId
    ) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        CategoryTypeEntity category = categoryTypeRepository
                .findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found"));

        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .name(request.name())
                .value(request.value())
                .frequency(request.frequency())
                .startDate(request.startDate())
                .nextCharge(getNextCharge(request))
                .subscriptionStatus(ACTIVE)
                .user(user)
                .categoryType(category)
                .build();

        return toResponse(subscriptionRepository.save(subscription));
    }

    // Atualiza somente uma assinatura pertencente ao usuario autenticado
    public SubscriptionResponse updateSubscription(
            Long id,
            Long categoryId,
            SubscriptionRequest request
    ) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        SubscriptionEntity subscription = subscriptionRepository
                .findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() ->
                        new SubscriptionNotFoundException("Subscription not found"));

        CategoryTypeEntity category = categoryTypeRepository
                .findByIdAndUser_Id(categoryId, user.getId())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found"));

        subscription.setName(request.name());
        subscription.setValue(request.value());
        subscription.setFrequency(request.frequency());
        subscription.setStartDate(request.startDate());
        subscription.setNextCharge(getNextCharge(request));
        subscription.setCategoryType(category);

        return toResponse(subscriptionRepository.save(subscription));
    }

    // Exclui somente uma assinatura pertencente ao usuario autenticado
    public void deleteSubscription(Long id) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        SubscriptionEntity subscription = subscriptionRepository
                .findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() ->
                        new SubscriptionNotFoundException("Subscription not found"));

        subscriptionRepository.delete(subscription);
    }

    // Lista somente as assinaturas do usuario autenticado
    public List<SubscriptionResponse> listSubscriptions() {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        return subscriptionRepository.findAllByUser_Id(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Converte entidade para DTO de resposta
    private SubscriptionResponse toResponse(SubscriptionEntity subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getName(),
                subscription.getValue(),
                subscription.getFrequency(),
                subscription.getStartDate(),
                subscription.getNextCharge(),
                subscription.getSubscriptionStatus(),
                subscription.getUser().getId(),
                subscription.getUser().getName(),
                subscription.getCategoryType().getId(),
                subscription.getCategoryType().getName()
        );
    }
}