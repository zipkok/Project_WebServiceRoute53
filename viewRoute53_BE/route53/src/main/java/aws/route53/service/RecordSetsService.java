package aws.route53.service;


import aws.route53.entity.RecordSetsEntity;
import aws.route53.entity.RecordSetsItemsEntity;
import aws.route53.repository.RecordSetsItemsRepository;
import aws.route53.repository.RecordSetsRepository;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.route53.Route53Client;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsRequest;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsResponse;
import software.amazon.awssdk.services.route53.model.ResourceRecord;
import software.amazon.awssdk.services.route53.model.ResourceRecordSet;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordSetsService {

    @Autowired
    RecordSetsRepository recordSetsRepository;

    @Autowired
    RecordSetsItemsRepository recordSetsItemsRepository;

    public List<RecordSetsEntity> getRecordSets() throws Exception {
        return recordSetsRepository.findAllByOrderByHostedZoneIdDesc();
    }

    public List<RecordSetsEntity> getRecordSetsWithHostedZoneId(String HostedZoneId) throws  Exception {
        return recordSetsRepository.findByHostedZoneIdOrderByRecordSetsIdxDesc(HostedZoneId);

    }

    @Transactional
    public void deleteRecordSetsWithHostedZoneId(String HostedZoneId) throws Exception {
        // Delete FROM tb_record_sets WHERE HostedZoneId = ?
        recordSetsRepository.deleteByHostedZoneIdOrderByRecordSetsIdxDesc(HostedZoneId);
    }

    @Transactional
    public void createRecordSets(String hostedZoneId) throws Exception {
        System.out.println("==========START=======RecordSetsService.createRecordSets 수행===============");

        // --profile -- region
        Region region = Region.AWS_GLOBAL;
        Route53Client route53Client = Route53Client.builder()
                .region(region)
                .build();

        // aws route53 list-resource-recordsets
        ListResourceRecordSetsRequest request = ListResourceRecordSetsRequest.builder()
                .hostedZoneId(hostedZoneId)
                .build();

        ListResourceRecordSetsResponse listResourceRecordSets = route53Client.listResourceRecordSets(request);
        List<ResourceRecordSet> records = listResourceRecordSets.resourceRecordSets();

        // Data DB Insert
        for (ResourceRecordSet record : records) {
            // RecordSetsItem toString And Apply regular expression
            String replaceResouceRecords = record.resourceRecords().toString()
                    .replaceAll("\\[ResourceRecord", "")
                    .replaceAll("ResourceRecord", "")
                    .replaceAll("(Value=)","")
                    .replaceAll("\\(", "")
                    .replaceAll("\\)", "")
                    .replaceAll("]", "");

            // RecordSetsItemsEntity 생성
            RecordSetsItemsEntity recordSetsItems =
                    RecordSetsItemsEntity.createRecordSetsItems(replaceResouceRecords);
            
            // RecordSetsEntity 생성
            RecordSetsEntity recordSets = RecordSetsEntity.createRecordSets(
                    record.name(),
                    record.typeAsString(),
                    record.ttl(),
                    hostedZoneId,
                    recordSetsItems
            );
            
            // Repository로 저장
            recordSetsRepository.save(recordSets);
        }

        // AWSCLI close()
        route53Client.close();
    }

    public void updateRecordSetss(String hostedZoneId) throws Exception {
        // --profile -- region
        Region region = Region.AWS_GLOBAL;
        Route53Client route53Client = Route53Client.builder()
                .region(region)
                .build();

        // aws route53 list-resource-recordsets
        ListResourceRecordSetsRequest request = ListResourceRecordSetsRequest.builder()
                .hostedZoneId(hostedZoneId)
                .build();

        ListResourceRecordSetsResponse listResourceRecordSets = route53Client.listResourceRecordSets(request);
        List<ResourceRecordSet> records = listResourceRecordSets.resourceRecordSets();

        for (ResourceRecordSet record : records) {
            System.out.println("The Record name is: " + record.name());
        }

        // AWSCLI close()
        route53Client.close();
    }

}
