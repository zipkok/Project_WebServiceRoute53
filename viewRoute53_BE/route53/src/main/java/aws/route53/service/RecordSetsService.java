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
            // RecordSetsItemsEntity ??????
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


            // Routing Policy??? GeoLocation ??????
            if (record.geoLocation() != null) {
                if(record.geoLocation().continentCode() != null) {
                    routingGeoLocation = "??????: " + record.geoLocation().continentCode();
                } else {
                    routingGeoLocation = "??????: " + record.geoLocation().countryCode();
                }
                routingPolicy = "GeoLocation";
            }

            // Routing Policy??? Latency ??????
            if (record.region() != null) {
                routingLatencyRegion = record.region().toString();
                routingPolicy = "Latency";
            }

            if (record.weight() != null) {
                routeWeight = record.weight().toString();
                routingPolicy = "Weight";
            }

            // RecordSetsEntity ??????
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

            // Repository??? ??????
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

        // DB -> List -> HashMap, dbHashMap??? ??????.
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

        // ????????? ?????? records ????????? ??????, HashMap ??? awscli ??????
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

            // Routing Policy??? GeoLocation ??????
            if (record.geoLocation() != null) {
                if(record.geoLocation().continentCode() != null) {
                    routingGeoLocation = "??????: " + record.geoLocation().continentCode();
                } else {
                    routingGeoLocation = "??????: " + record.geoLocation().countryCode();
                }
                routingPolicy = "GeoLocation";
            }

            // Routing Policy??? Latency ??????
            if (record.region() != null) {
                routingLatencyRegion = record.region().toString();
                routingPolicy = "Latency";
            }

            if (record.weight() != null) {
                routeWeight = record.weight().toString();
                routingPolicy = "Weight";
            }

            // ?????? ????????? ??????
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

            // Record??? HashMap??? ???????????? True (?????? ????????? ???)
            // ?????????: recordName, type, expire, recordSetsItems
            if(dbListToHashMap.containsValue(CompareRecordSets)) {
                System.out.println("================================");
                System.out.println("Not Deleted " + record.name() + " / " + recordSetsItemsToString);
                // ????????? ?????? ????????? ??????.

            // Record??? HashMap??? ???????????? ????????? False (????????? ????????? ?????????)
            } else {
                System.out.println("================================");
                System.out.println("Remove ... " + record.name() + " / " + recordSetsItemsToString);
                System.out.println("================================");
                removeRecordSetsWithRecordName(record.name());
                // recordSetsRepository.save(recordSets);
            }
        }

        // ????????? ?????? records ????????? ??????, HashMap ??? awscli ??????
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

            // Routing Policy??? GeoLocation ??????
            if (record.geoLocation() != null) {
                if(record.geoLocation().continentCode() != null) {
                    routingGeoLocation = "??????: " + record.geoLocation().continentCode();
                } else {
                    routingGeoLocation = "??????: " + record.geoLocation().countryCode();
                }
                routingPolicy = "GeoLocation";
            }

            // Routing Policy??? Latency ??????
            if (record.region() != null) {
                routingLatencyRegion = record.region().toString();
                routingPolicy = "Latency";
            }

            if (record.weight() != null) {
                routeWeight = record.weight().toString();
                routingPolicy = "Weight";
            }

            // ?????? ????????? ??????
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

            // Record??? HashMap??? ???????????? True (?????? ????????? ???)
            // ?????????: recordName, type, expire, recordSetsItems
            if(dbListToHashMap.containsValue(CompareRecordSets)) {
                System.out.println("================================");
                System.out.println("Not Create ... " + record.name() + " / " + recordSetsItemsToString);
                // ????????? ?????? ????????? ??????.

            // Record??? HashMap??? ???????????? ????????? False (????????? ????????? ?????????)
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
