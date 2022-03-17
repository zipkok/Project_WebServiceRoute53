package aws.route53.controller;

import aws.route53.dto.AccountDto;
import aws.route53.dto.RecordSetsDto;
import aws.route53.entity.AccountEntity;
import aws.route53.entity.RecordSetsEntity;
import aws.route53.service.AccountService;
import aws.route53.service.ListHostedZones;
import aws.route53.service.ListResourceRecordSets;
import aws.route53.service.RecordSetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.route53.model.ResourceRecordSet;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class RecordSetsController {

    @Autowired
    RecordSetsService recordSetsService;

    @Autowired
    ListResourceRecordSets listResourceRecordSets;

    /*
     * HostedZoneId를 기준으로
     * DB에서 데이터 조회 후 Response
     */
    @GetMapping("/recordsets/{HostedZoneId}")
    public List<RecordSetsDto> getRecordSetsWithHostedZoneId(@PathVariable String HostedZoneId) throws Exception {
        List<RecordSetsEntity> recordsets = recordSetsService.getRecordSetsWithHostedZoneId(HostedZoneId);
        List<RecordSetsDto> result = recordsets.stream()
                .map(o -> new RecordSetsDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /*
     * 신규 Account 등록 시
     * awscli -> DB Insert
     */
    @PostMapping("/recordsets/{HostedZoneId}")
    public void batchRecordSets(@PathVariable @Valid String HostedZoneId) throws Exception {
        recordSetsService.createRecordSets(HostedZoneId);
    }

    /*
     * HostedZoneId와 일치하는 레코드 모두 삭제
     */
    @DeleteMapping("/recordsets/{HostedZoneId}")
    public void deleteRecordSetsWithHostedZoneId(@PathVariable @Valid String HostedZoneId) throws Exception {
        recordSetsService.deleteRecordSetsWithHostedZoneId(HostedZoneId);
    }

    /**
     * aws route53 list-resource-record-sets --hostsed-zoneid <@PathVariable String HostedZoneId>
     *     -> DB
     */
    @RequestMapping(value = "/recordsetss/{HostedZoneId}", method= RequestMethod.POST)
    public List<MainController.listResourceRecordDto> getListRecordSetsList(@PathVariable String HostedZoneId) {
        // awscli 로 HostedZoneId에 따라 DB에 저장한다.
        // RecordSets에 저장해야함..
        // RecordSetsItems에 저장해야함..
        List<ResourceRecordSet> result = listResourceRecordSets.ListResourceRecord(HostedZoneId);
        List<MainController.listResourceRecordDto> results = result.stream()
                .map(o -> new MainController.listResourceRecordDto(o))
                .collect(Collectors.toList());

        // DB 내용을 List로 가져온다
        // DTO로 반환한다.

        return results;
    }
}
