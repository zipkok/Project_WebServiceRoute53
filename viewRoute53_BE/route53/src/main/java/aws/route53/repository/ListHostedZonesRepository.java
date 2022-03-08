package aws.route53.repository;

import aws.route53.Entity.ListHostedZonesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListHostedZonesRepository extends CrudRepository<ListHostedZonesEntity, Integer> {

    List<ListHostedZonesEntity> findAllByOrderByAccountIdxDesc();

}
