package aws.route53.controller;

import aws.route53.domain.Result;
import aws.route53.dto.CommonResponse;
import aws.route53.dto.CompareRecordSetsDto;
import aws.route53.dto.RecordSetsDto;
import aws.route53.entity.AccountEntity;
import aws.route53.service.AccountService;
import aws.route53.service.RecordSetsService;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
public class RecordSetsController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RecordSetsService recordSetsService;

    @Autowired
    AccountService accountService;


    /**
     * DB에서 모든 레코드를 조회 후 Response
     */
    @GetMapping("/recordsets/")
    public List<RecordSetsDto> getRecordSets() throws Exception {
        return recordSetsService.getRecordSets();
    }

    /**
     * HostedZoneId를 기준으로 DB에서 데이터 조회 후 Response
     */
    @GetMapping("/recordsets/{HostedZoneId}")
    public List<RecordSetsDto> getRecordSetsWithHostedZoneId(@PathVariable @Valid String HostedZoneId) throws Exception {
        return recordSetsService.getRecordSetsWithHostedZoneId(HostedZoneId);
    }

    /**
     * 신규 Account 등록 시 awscli -> DB Insert
     */
    @PostMapping("/recordsets/{HostedZoneId}")
    public CommonResponse<JsonNode> createRecordSets(@PathVariable @Valid String HostedZoneId) throws Exception {
        List<AccountEntity> credentials = accountService.getAccountCredentials(HostedZoneId);
        try {
            recordSetsService.createRecordSets(HostedZoneId,
                    credentials.get(0).getAws_access_key(),
                    credentials.get(0).getAws_secret_key());
        } catch (Exception e) {
            log.error("CreateRecordSets Exception: {}", e);
            return new CommonResponse<>(Result.FAIL);
        }
        return new CommonResponse<>(Result.SUCCESS);

    }

    /**
     * HostedZoneId와 일치하는 레코드 모두 삭제
     */
    @DeleteMapping("/recordsets/{HostedZoneId}")
    public void deleteRecordSetsWithHostedZoneId(@PathVariable @Valid String HostedZoneId) throws Exception {
        recordSetsService.deleteRecordSetsWithHostedZoneId(HostedZoneId);
    }

    /**
     *
     * @param HostedZoneId
     * @return updateRecordSets(HostedZoneId, getAws_access_key, getAws_secret_key)
     * @throws Exception
     */
    @PutMapping("/recordsets/{HostedZoneId}")
    public HashMap<Integer, CompareRecordSetsDto> update(@PathVariable @Valid String HostedZoneId) throws Exception {
        List<AccountEntity> credentials = accountService.getAccountCredentials(HostedZoneId);
        return recordSetsService.updateRecordSets(HostedZoneId,
                                            credentials.get(0).getAws_access_key(),
                                            credentials.get(0).getAws_secret_key());
    }
}
