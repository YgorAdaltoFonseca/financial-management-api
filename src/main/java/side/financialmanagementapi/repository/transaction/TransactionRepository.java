package side.financialmanagementapi.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import side.financialmanagementapi.entities.transaction.TransactionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> ,
        JpaSpecificationExecutor<TransactionEntity> {

    List<TransactionEntity> findAllByUser_Id(Long userId);

    Optional<TransactionEntity> findByIdAndUser_Id(Long id, Long userId);

}
