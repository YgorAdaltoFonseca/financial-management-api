package side.financialmanagementapi.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import side.financialmanagementapi.entities.transaction.TransactionEntity;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> ,
        JpaSpecificationExecutor<TransactionEntity> {

    List<TransactionEntity> findAllByUserId(Long userId);

}
