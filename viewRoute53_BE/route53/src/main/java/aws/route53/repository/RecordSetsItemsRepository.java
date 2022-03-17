package aws.route53.repository;

import aws.route53.entity.RecordSetsEntity;
import aws.route53.entity.RecordSetsItemsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecordSetsItemsRepository extends CrudRepository<RecordSetsItemsEntity, Integer> {
    List<RecordSetsItemsEntity> findAllByOrderByRecordSetsItemsIdxDesc();
}
