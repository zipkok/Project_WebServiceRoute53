package aws.route53.service;

import aws.route53.dto.CompareRecordSetsDto;
import aws.route53.dto.RecordSetsDto;
import aws.route53.entity.RecordSetsEntity;

import aws.route53.repository.RecordSetsRepository;

import ch.qos.logback.core.net.SyslogOutputStream;
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

    @Autowired
    AwsCredentials awsCredentials;

    public List<RecordSetsDto> getRecordSets() throws Exception {
        List<RecordSetsEntity> recordsets =recordSetsRepository.findAllByOrderByHostedZoneIdDesc();
        return recordsets.stream()
                .map(o -> new RecordSetsDto(o))
                .collect(Collectors.toList());
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

    /**
     * @param RecordName
     * @return void
     * @throws Exception
     */
    @Transactional
    public void removeRecordSetsWithRecordName(String RecordName) throws Exception {
        recordSetsRepository.deleteByRecordNameOrderByRecordSetsIdxDesc(RecordName);
    }

    /**
     * @param hostedZoneId
     * @param awsAccessKey
     * @param awsSecretKey
     * @return void
     * @throws Exception
     */
    public void createRecordSets(String hostedZoneId, String awsAccessKey, String awsSecretKey) throws Exception {
        awsCredentials.setCredentials(awsAccessKey, awsSecretKey);
        awsCredentials.setRoute53Client();
        List<ResourceRecordSet> records = awsCredentials.getRoute53Info(hostedZoneId);

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
    }

    /**
     *
     * @param hostedZoneId
     * @param awsAccessKey
     * @param awsSecretKey
     * @return dbListToHashMap
     * @throws Exception
     */
    @Transactional
    public HashMap<Integer, CompareRecordSetsDto> updateRecordSets(String hostedZoneId,
                                                                   String awsAccessKey,
                                                                   String awsSecretKey) throws Exception {
        awsCredentials.setCredentials(awsAccessKey, awsSecretKey);
        awsCredentials.setRoute53Client();
        List<ResourceRecordSet> records = awsCredentials.getRoute53Info(hostedZoneId);

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
            CompareRecordSetsDto CompareRecordSets = CompareRecordSetsDto.CreateCompareRecordSetsDto(
                    record.name(),
                    record.typeAsString(),
                    record.ttl(),
                    recordSetsItemsToString,
                    routingPolicy,
                    routingGeoLocation,
                    routingLatencyRegion,
                    routeWeight
            );

            // Record가 HashMap에 존재하면 True (이미 등록된 값)
            // 비교값: recordName, type, expire, recordSetsItems
            if(dbListToHashMap.containsValue(CompareRecordSets)) {
                System.out.println("================================");
                System.out.println("Not Deleted " + record.name() + " / " + recordSetsItemsToString);
                // 변경된 값이 있는지 확인.

            // Record가 HashMap에 존재하지 않으면 False (수정이 필요한 데이터)
            } else {
                System.out.println("================================");
                System.out.println("Remove ... " + record.name() + " / " + recordSetsItemsToString);
                System.out.println("================================");
                removeRecordSetsWithRecordName(record.name());
                // recordSetsRepository.save(recordSets);
            }
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
            CompareRecordSetsDto CompareRecordSets = CompareRecordSetsDto.CreateCompareRecordSetsDto(
                    record.name(),
                    record.typeAsString(),
                    record.ttl(),
                    recordSetsItemsToString,
                    routingPolicy,
                    routingGeoLocation,
                    routingLatencyRegion,
                    routeWeight
            );

            // Record가 HashMap에 존재하면 True (이미 등록된 값)
            // 비교값: recordName, type, expire, recordSetsItems
            if(dbListToHashMap.containsValue(CompareRecordSets)) {
                System.out.println("================================");
                System.out.println("Not Create ... " + record.name() + " / " + recordSetsItemsToString);
                // 변경된 값이 있는지 확인.

            // Record가 HashMap에 존재하지 않으면 False (수정이 필요한 데이터)
            } else {
                System.out.println("================================");
                System.out.println("Create ... " + record.name() + " / " + recordSetsItemsToString);
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
        return dbListToHashMap;
    }
}
