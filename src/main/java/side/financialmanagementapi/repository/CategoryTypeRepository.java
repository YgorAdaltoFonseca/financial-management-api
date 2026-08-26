package side.financialmanagementapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import side.financialmanagementapi.entities.CategoryTypeEntity;

@Repository
public interface CategoryTypeRepository extends JpaRepository<CategoryTypeEntity, Long>,
        JpaSpecificationExecutor<CategoryTypeEntity> {
}
