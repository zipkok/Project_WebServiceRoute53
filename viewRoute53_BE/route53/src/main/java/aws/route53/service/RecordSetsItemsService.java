package aws.route53.service;

import aws.route53.entity.RecordSetsEntity;
import aws.route53.entity.RecordSetsItemsEntity;
import aws.route53.repository.RecordSetsItemsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordSetsItemsService {

    @Autowired
    RecordSetsItemsRepository recordSetsItemsRepository;

    public List<RecordSetsItemsEntity> getRecordSetsItemsList() throws Exception {
        return recordSetsItemsRepository.findAllByOrderByRecordSetsItemsIdxDesc();
    }

    public void save(RecordSetsItemsEntity recordSetsItemsEntity) throws Exception {
        recordSetsItemsRepository.save(recordSetsItemsEntity);
    }

    public void update(RecordSetsItemsEntity recordSetsItemsEntity) throws Exception {
        recordSetsItemsRepository.save(recordSetsItemsEntity);
    }
}
