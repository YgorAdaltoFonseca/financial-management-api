
package side.financialmanagementapi.repository.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import side.financialmanagementapi.entities.subscription.SubscriptionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository
        extends JpaRepository<SubscriptionEntity, Long>,
        JpaSpecificationExecutor<SubscriptionEntity> {

    List<SubscriptionEntity> findAllByUser_Id(Long userId);

    Optional<SubscriptionEntity> findByIdAndUser_Id(
            Long id,
            Long userId
    );
}