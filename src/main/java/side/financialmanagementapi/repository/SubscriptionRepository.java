package side.financialmanagementapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import side.financialmanagementapi.entities.SubscriptionEntity;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long>,
        JpaSpecificationExecutor<SubscriptionEntity> {
}
