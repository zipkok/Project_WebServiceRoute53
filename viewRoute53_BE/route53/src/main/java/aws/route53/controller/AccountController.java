package aws.route53.controller;

import aws.route53.dto.AccountDto;
import aws.route53.entity.AccountEntity;
import aws.route53.service.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class AccountController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AccountService accountService;

    /**
     * account 조회
     */
    @GetMapping("/account")
    public ResponseEntity<List<AccountDto>> getAccount() throws Exception {
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }

    /**
     * accountIdx 조회
     */
    @GetMapping("/account/idx/{accountIdx}")
    public ResponseEntity<List<AccountEntity>> getAccountByAccountIdx(@PathVariable Integer accountIdx) throws Exception {
        return ResponseEntity.ok().body(accountService.getAccountByAccountIdx(accountIdx));
    }

    /** Id를 기준으로 데이터 삭제
     */
    @DeleteMapping("/account/idx/{accountIdx}")
    public ResponseEntity deleteAccount(@PathVariable Integer accountIdx) throws Exception {
        accountService.deleteAccountWithId(accountIdx);
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }

    /**
     * DB에서 Data 조회 후 데이터 Insert
     */
    @PostMapping("/account")
    public ResponseEntity saveAccount(@RequestBody AccountDto req) throws Exception {
        accountService.saveAccount(req);
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }

    /**
     * Account 정보 수정
     */
    @GetMapping("/account/{HostedZoneId}")
    public ResponseEntity updateToGetAccount(@RequestBody AccountDto req,
                                             @PathVariable String HostedZoneId) throws Exception {
        // getId로 회원 조회 후
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }

    /**
     * Account 정보 수정
     */
    @PutMapping("/account/idx/{accountIdx}")
    public ResponseEntity updateToPutAccount(@RequestBody AccountDto req) throws Exception {
        // getId로 회원 조회 후
        accountService.putAccount(req);
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }
}
