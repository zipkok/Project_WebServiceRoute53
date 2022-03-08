package aws.route53.controller;

import aws.route53.service.ListHostedZones;
import aws.route53.service.ListResourceRecordSets;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.route53.model.ResourceRecord;
import software.amazon.awssdk.services.route53.model.ResourceRecordSet;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class MainController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ListHostedZones listHostedZones;

    @Autowired
    ListResourceRecordSets listResourceRecordSets;

    @RequestMapping(value = "/", method= RequestMethod.GET)
    public String home() {

        return "Hello";
    }

    @RequestMapping(value = "/list", method= RequestMethod.GET)
    public List<HashMap<Object, Object>> list() {
        return listHostedZones.ListHostedZones();
    }

    @RequestMapping(value = "/recordsets/{HostedZoneId}", method = RequestMethod.GET)
    public List<listResourceRecordDto> getRecordSets(@PathVariable String HostedZoneId) {

        List<ResourceRecordSet> result = listResourceRecordSets.ListResourceRecord(HostedZoneId);
        List<listResourceRecordDto> results = result.stream()
                .map(o -> new listResourceRecordDto(o))
                .collect(Collectors.toList());

        return results;
    }

    @Data
    static class listResourceRecordDto {
        private String recordName;
        private String type;
        private String ResourceRecords;
        private Long recordExpire;

        public listResourceRecordDto(ResourceRecordSet resourceRecordSet) {
            recordName = resourceRecordSet.name();
            type = resourceRecordSet.typeAsString();
            ResourceRecords = null;
            recordExpire = resourceRecordSet.ttl();
        }
    }
}
