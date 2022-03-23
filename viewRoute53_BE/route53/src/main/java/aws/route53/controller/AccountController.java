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
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class AccountController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AccountService accountService;

    /*
     * account 조회
     */
    @GetMapping("/account")
    public ResponseEntity<List<AccountDto>> getAccount() throws Exception {
        return ResponseEntity.ok().body(accountService.getAccount());
    }

    /*
     * DB에서 Data 조회 후 데이터 Insert
     */
    @PostMapping("/account")
    public ResponseEntity saveAccount(@RequestBody AccountDto req) throws Exception {
        accountService.save(req);
        return ResponseEntity.ok().body(accountService.getAccount());
    }

    @PutMapping("/account")
    public ResponseEntity updateAccount(@RequestBody AccountDto req) throws Exception {
        // Account 정보 업데이트
        return ResponseEntity.ok().body(accountService.getAccount());
    }
}
