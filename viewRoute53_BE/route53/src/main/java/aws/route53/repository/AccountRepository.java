package aws.route53.repository;

import aws.route53.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Integer> {
                List<AccountEntity> findAllByOrderByAccountIdxDesc();

                List<AccountEntity> findByAccountIdxOrderByAccountIdxDesc(Integer accountIdx);

                List<AccountEntity> findByHostedZoneIdOrderByAccountIdxDesc(String HostedZonId);
}
