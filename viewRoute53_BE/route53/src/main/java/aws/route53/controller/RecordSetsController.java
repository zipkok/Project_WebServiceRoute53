package aws.route53.controller;

import aws.route53.dto.CompareRecordSetsDto;
import aws.route53.dto.RecordSetsDto;
import aws.route53.entity.AccountEntity;
import aws.route53.entity.RecordSetsEntity;
import aws.route53.service.AccountService;
import aws.route53.service.RecordSetsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    AccountService accountService;

    @GetMapping("/recordsets/test/{HostedZoneId}")
    public void test(@PathVariable @Valid String HostedZoneId) throws Exception {
        List<AccountEntity> credentials = accountService.getAccountCredentials(HostedZoneId);
        recordSetsService.createRecordSetsTest(HostedZoneId,
                credentials.get(0).getAws_access_key(),
                credentials.get(0).getAws_secret_key());

    }

    /**
     * HostedZoneId를 기준으로 DB에서 데이터 조회 후 Response
     */
    @GetMapping("/recordsets/{HostedZoneId}")
    public List<RecordSetsDto> getRecordSetsWithHostedZoneId(@PathVariable String HostedZoneId) throws Exception {
        List<RecordSetsEntity> recordsets = recordSetsService.getRecordSetsWithHostedZoneId(HostedZoneId);
        List<RecordSetsDto> result = recordsets.stream()
                .map(o -> new RecordSetsDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 신규 Account 등록 시 awscli -> DB Insert
     */
    @PostMapping("/recordsets/{HostedZoneId}")
    public void createRecordSets(@PathVariable @Valid String HostedZoneId) throws Exception {
        List<AccountEntity> credentials = accountService.getAccountCredentials(HostedZoneId);
        recordSetsService.createRecordSets(HostedZoneId,
                                            credentials.get(0).getAws_access_key(),
                                            credentials.get(0).getAws_secret_key());
    }

    /**
     * HostedZoneId와 일치하는 레코드 모두 삭제
     */
    @DeleteMapping("/recordsets/{HostedZoneId}")
    public void deleteRecordSetsWithHostedZoneId(@PathVariable @Valid String HostedZoneId) throws Exception {
        recordSetsService.deleteRecordSetsWithHostedZoneId(HostedZoneId);
    }

    /**
     * Update
     */
    @PutMapping("/recordsets/test/{HostedZoneId}")
    public HashMap<Integer, CompareRecordSetsDto> update(@PathVariable @Valid String HostedZoneId) throws Exception {
        List<AccountEntity> credentials = accountService.getAccountCredentials(HostedZoneId);
        return recordSetsService.updateRecordSets(HostedZoneId,
                                            credentials.get(0).getAws_access_key(),
                                            credentials.get(0).getAws_secret_key());
    }
}
