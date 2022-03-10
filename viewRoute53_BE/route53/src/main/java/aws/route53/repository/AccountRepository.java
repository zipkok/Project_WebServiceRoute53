package aws.route53.repository;

import aws.route53.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<AccountEntity, Integer> {
    List<AccountEntity> findAllByOrderByAccountIdxDesc();
}
