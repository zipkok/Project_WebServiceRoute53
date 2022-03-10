package aws.route53.controller;

import aws.route53.dto.AccountDto;
import aws.route53.entity.AccountEntity;
import aws.route53.service.AccountService;
import aws.route53.service.ListHostedZones;
import aws.route53.service.ListResourceRecordSets;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
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
        // TODO: 조회는 어떻게?
        //
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

    @Autowired
    AccountService accountService;

    /**
     * 완료 / account 조회
     */
    @RequestMapping(value = "/account", method= RequestMethod.GET)
    public List<AccountDto> getAccountList() throws Exception {
        List<AccountEntity> accounts = accountService.getAccountList();
        List<AccountDto> result = accounts.stream().map(o -> new AccountDto(o)).collect(Collectors.toList());
        // TODO: 조회는 어떻게?
        //
        return result;
    }

    /**
     * 예정 / account 등록
     */
    @RequestMapping(value = "/list", method= RequestMethod.POST)
    public List<HashMap<Object, Object>> saveAccountList() {
        // TODO:
        // Front로부터 Account 정보를 받아서 등록한다.
        // AccessKey, Secret Key 값이 있어야 함.
        return listHostedZones.ListHostedZones();
    }

    /**
     * 예정 / HostedZoneId를 활용한
     */
    @RequestMapping(value = "/recordsets/{HostedZoneId}", method= RequestMethod.GET)
    public List<listResourceRecordDto> getListRecordSetsList(@PathVariable String HostedZoneId) {
        // TODO:
        // awscli 로 HostedZoneId에 따라 DB에 저장한다.
        List<ResourceRecordSet> result = listResourceRecordSets.ListResourceRecord(HostedZoneId);
        List<listResourceRecordDto> results = result.stream()
                .map(o -> new listResourceRecordDto(o))
                .collect(Collectors.toList());

        // DB 내용을 List로 가져온다
        // DTO로 반환한다.

        return results;
    }
}
