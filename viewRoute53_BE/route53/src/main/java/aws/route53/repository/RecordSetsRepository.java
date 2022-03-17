package aws.route53.repository;

import aws.route53.entity.AccountEntity;
import aws.route53.entity.RecordSetsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordSetsRepository  extends CrudRepository<RecordSetsEntity, Integer> {

    List<RecordSetsEntity> findAllByOrderByRecordSetsIdxDesc();

    List<RecordSetsEntity> findAllByOrderByHostedZoneIdDesc();

    List<RecordSetsEntity> findByHostedZoneIdOrderByRecordSetsIdxDesc(String hostedZoneId);

    // Delete FROM tb_record_sets WHERE HostedZoneId = ?
    void deleteByHostedZoneIdOrderByRecordSetsIdxDesc(String hostedZoneId);
}
