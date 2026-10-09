package side.financialmanagementapi.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import side.financialmanagementapi.entities.category.CategoryTypeEntity;

import java.util.List;

@Repository
public interface CategoryTypeRepository extends JpaRepository<CategoryTypeEntity, Long>,
        JpaSpecificationExecutor<CategoryTypeEntity> {

    List<CategoryTypeEntity> findAllByUserId(Long userId);
}
