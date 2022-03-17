package aws.route53.controller;

import aws.route53.dto.AccountDto;
import aws.route53.entity.AccountEntity;
import aws.route53.service.AccountService;
import aws.route53.service.ListHostedZones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class AccountController {

    @Autowired
    AccountService accountService;

    /**
     * account 조회
     */
    @GetMapping("/account")
    public List<AccountDto> getAccount() throws Exception {
        List<AccountEntity> accounts = accountService.getAccount();
        List<AccountDto> result = accounts.stream()
                .map(o -> new AccountDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * DB에서 Data 조회 후 데이터 Insert
     */
    @PostMapping("/account")
    public void saveAccount(@RequestBody AccountDto req) throws Exception {
        System.out.println("=========================================");
        System.out.println("req: " + req);
        accountService.save(req);
    }
}
