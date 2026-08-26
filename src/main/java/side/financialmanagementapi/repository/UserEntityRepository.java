package side.financialmanagementapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import side.financialmanagementapi.entities.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> ,
        JpaSpecificationExecutor<UserEntity> {

}
