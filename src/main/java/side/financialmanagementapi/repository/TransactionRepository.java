package side.financialmanagementapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import side.financialmanagementapi.entities.SubscriptionEntity;
import side.financialmanagementapi.entities.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> ,
        JpaSpecificationExecutor<TransactionEntity> {
}
