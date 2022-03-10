package aws.route53.service;

import aws.route53.entity.AccountEntity;
import aws.route53.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public List<AccountEntity> getAccountList() throws Exception {
        return accountRepository.findAllByOrderByAccountIdxDesc();
    }
}
