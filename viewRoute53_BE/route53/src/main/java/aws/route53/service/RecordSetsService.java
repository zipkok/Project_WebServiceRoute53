package aws.route53.service;

import aws.route53.dto.CompareRecordSetsDto;
import aws.route53.dto.RecordSetsDto;
import aws.route53.entity.RecordSetsEntity;

import aws.route53.repository.RecordSetsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.route53.Route53Client;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsRequest;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsResponse;
import software.amazon.awssdk.services.route53.model.ResourceRecord;
import software.amazon.awssdk.services.route53.model.ResourceRecordSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordSetsService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RecordSetsRepository recordSetsRepository;

    public List<RecordSetsEntity> getRecordSets() throws Exception {
        return recordSetsRepository.findAllByOrderByHostedZoneIdDesc();
    }

    public List<RecordSetsDto> getRecordSetsWithHostedZoneId(String HostedZoneId) throws  Exception {
        List<RecordSetsEntity> recordsets =recordSetsRepository.findByHostedZoneIdOrderByRecordSetsIdxDesc(HostedZoneId);
        return recordsets.stream()
                        .map(o -> new RecordSetsDto(o))
                        .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRecordSetsWithHostedZoneId(String HostedZoneId) throws Exception {
        // Delete FROM tb_record_sets WHERE HostedZoneId = ?
        recordSetsRepository.deleteByHostedZoneIdOrderByRecordSetsIdxDesc(HostedZoneId);
    }

    public void createRecordSets(String hostedZoneId, String awsAccessKey, String awsSecretKey) throws Exception {
        // --profile
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);

        // -- region
        Region region = Region.AWS_GLOBAL;

        // Execute awscli
        Route53Client route53Client = Route53Client
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(region)
                .build();

        // aws route53 list-resource-recordsets
        ListResourceRecordSetsRequest request = ListResourceRecordSetsRequest.builder()
                .hostedZoneId(hostedZoneId)
                .build();

        ListResourceRecordSetsResponse listResourceRecordSets = route53Client.listResourceRecordSets(request);
        List<ResourceRecordSet> records = listResourceRecordSets.resourceRecordSets();

        System.out.println("records" + records);

        // Data DB Insert
        for (ResourceRecordSet record : records) {
            // RecordSetsItemsEntity 생성
            List<String> recordSetsItemsParseToList = new ArrayList<>();

            for (ResourceRecord a :  record.resourceRecords() ) {
                recordSetsItemsParseToList.add(a.value());
            }
            String recordSetsItemsToString = recordSetsItemsParseToList
                    .toString()
                    .replace("[","")
                    .replace("]","");


            String routingPolicy = "Simple";
            String routingGeoLocation = "-";
            String routingLatencyRegion = "-";
            String routeWeight = "No";


            // Routing Policy가 GeoLocation 경우
            if (record.geoLocation() != null) {
                if(record.geoLocation().continentCode() != null) {
                    routingGeoLocation = "대륙: " + record.geoLocation().continentCode();
                } else {
                    routingGeoLocation = "국가: " + record.geoLocation().countryCode();
                }
                routingPolicy = "GeoLocation";
            }

            // Routing Policy가 Latency 경우
            if (record.region() != null) {
                routingLatencyRegion = record.region().toString();
                routingPolicy = "Latency";
            }

            if (record.weight() != null) {
                routeWeight = record.weight().toString();
                routingPolicy = "Weight";
            }

            // RecordSetsEntity 생성
            RecordSetsEntity recordSets = RecordSetsEntity.createRecordSets(
                    record.name(),
                    record.typeAsString(),
                    record.ttl(),
                    hostedZoneId,
                    recordSetsItemsToString,
                    routingPolicy,
                    routingGeoLocation,
                    routingLatencyRegion,
                    routeWeight
            );

            // Repository로 저장
            recordSetsRepository.save(recordSets);
        }

        // AWSCLI close()
        route53Client.close();
    }

    public HashMap<Integer, CompareRecordSetsDto> updateRecordSets(String hostedZoneId,
                                                                   String awsAccessKey,
                                                                   String awsSecretKey) throws Exception {

        // --profile
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);

        // -- region
        Region region = Region.AWS_GLOBAL;

        // Execute awscli
        Route53Client route53Client = Route53Client
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(region)
                .build();

        // aws route53 list-resource-recordsets
        ListResourceRecordSetsRequest request = ListResourceRecordSetsRequest.builder()
                .hostedZoneId(hostedZoneId)
                .build();

        ListResourceRecordSetsResponse listResourceRecordSets = route53Client.listResourceRecordSets(request);
        List<ResourceRecordSet> records = listResourceRecordSets.resourceRecordSets();

        // DB -> List -> HashMap, dbHashMap에 저장.
        HashMap<Integer, CompareRecordSetsDto> dbListToHashMap = new HashMap<>();
        List<RecordSetsEntity> dbList = recordSetsRepository.findAllByOrderByRecordSetsIdxDesc();

        for (RecordSetsEntity a : dbList) {
            CompareRecordSetsDto compareHashMap = new CompareRecordSetsDto();
            compareHashMap.setRecordName(a.getRecordName());
            compareHashMap.setType(a.getType());
            compareHashMap.setExpire(a.getExpire());
            compareHashMap.setRecordSetsValue((a.getRecordSetsValue()));
            compareHashMap.setRoutingPolicy(a.getRoutingPolicy());
            compareHashMap.setRouteGeoLocation(a.getRouteGeoLocation());
            compareHashMap.setRouteLatencyRegion(a.getRouteLatencyRegion());
            compareHashMap.setRouteWeight(a.getRouteWeight());
            dbListToHashMap.put(a.getRecordSetsIdx(), compareHashMap);
        }


        // 비교를 위해 records 데이터 정제, HashMap 과 awscli 비교
        for (ResourceRecordSet record : records) {
            List<String> recordSetsItemsParseToList = new ArrayList<>();

            for (ResourceRecord a :  record.resourceRecords() ) {
                recordSetsItemsParseToList.add(a.value());
            }
            String recordSetsItemsToString = recordSetsItemsParseToList
                    .toString()
                    .replace("[","")
                    .replace("]","");


            String routingPolicy = "Simple";
            String routingGeoLocation = "-";
            String routingLatencyRegion = "-";
            String routeWeight = "-";

            // Routing Policy가 GeoLocation 경우
            if (record.geoLocation() != null) {
                if(record.geoLocation().continentCode() != null) {
                    routingGeoLocation = "대륙: " + record.geoLocation().continentCode();
                } else {
                    routingGeoLocation = "국가: " + record.geoLocation().countryCode();
                }
                routingPolicy = "GeoLocation";
            }

            // Routing Policy가 Latency 경우
            if (record.region() != null) {
                routingLatencyRegion = record.region().toString();
                routingPolicy = "Latency";
            }

            if (record.weight() != null) {
                routeWeight = record.weight().toString();
                routingPolicy = "Weight";
            }

            // 비교 데이터 생성
            CompareRecordSetsDto expectedUpdateRecordSets = new CompareRecordSetsDto();
            expectedUpdateRecordSets.setRecordName(record.name());
            expectedUpdateRecordSets.setType(record.typeAsString());
            expectedUpdateRecordSets.setExpire(record.ttl());
            expectedUpdateRecordSets.setRecordSetsValue(recordSetsItemsToString);
            expectedUpdateRecordSets.setRoutingPolicy(routingPolicy);
            expectedUpdateRecordSets.setRouteGeoLocation(routingGeoLocation);
            expectedUpdateRecordSets.setRouteLatencyRegion(routingLatencyRegion);
            expectedUpdateRecordSets.setRouteWeight(routeWeight);

            // Record가 HashMap에 존재하면 True (이미 등록된 값)
            // 비교값: recordName, type, expire, recordSetsItems
            if(dbListToHashMap.containsValue(expectedUpdateRecordSets)) {
                System.out.println("================================");
                System.out.println("True" + expectedUpdateRecordSets);
                // 변경된 값이 있는지 확인.

            // Record가 HashMap에 존재하지 않으면 False (수정이 필요한 데이터)
            } else {
                System.out.println("================================");
                System.out.println("False" + expectedUpdateRecordSets);
                System.out.println("False" + dbListToHashMap.containsValue(expectedUpdateRecordSets));
                System.out.println("================================");
                RecordSetsEntity recordSets = RecordSetsEntity.createRecordSets(
                        record.name(),
                        record.typeAsString(),
                        record.ttl(),
                        hostedZoneId,
                        recordSetsItemsToString,
                        routingPolicy,
                        routingGeoLocation,
                        routingLatencyRegion,
                        routeWeight
                );
                recordSetsRepository.save(recordSets);
            }
        }

        // AWSCLI close()
        route53Client.close();

        return dbListToHashMap;
    }
}
