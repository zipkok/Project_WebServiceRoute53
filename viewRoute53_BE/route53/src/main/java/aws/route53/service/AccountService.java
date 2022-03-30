package aws.route53.service;

import aws.route53.dto.AccountDto;
import aws.route53.entity.AccountEntity;
import aws.route53.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public List<AccountEntity> getAllAccount() throws Exception {
        List<AccountEntity> accounts = accountRepository.findAllByOrderByAccountIdxDesc();
//        List<AccountDto> result = accounts.stream()
//                .map(o -> new AccountDto(o))
//                .collect(Collectors.toList());

        return accounts;
    }

    public List<AccountEntity> getAccountByAccountIdx(Integer accountIdx) throws Exception {
        List<AccountEntity> account = accountRepository.findByAccountIdxOrderByAccountIdxDesc(accountIdx);
        return account;
    }

    public void deleteAccountWithId(Integer accountIdx) throws Exception {
        accountRepository.deleteById(accountIdx);
    }

    public List<AccountEntity> getAccountCredentials(String HostedZoneId) throws Exception {
        return accountRepository.findByHostedZoneIdOrderByAccountIdxDesc(HostedZoneId);
    }

    public void save(AccountDto accountDto) throws Exception {
        // 신규 Account 정보 DB에 저장
        AccountEntity result = AccountEntity.createAccount(
                accountDto.getHostedZoneName(),
                accountDto.getHostedZoneId(),
                accountDto.getAccountName(),
                accountDto.getTeam(),
                accountDto.getAwsAccessKey(),
                accountDto.getAwsCredentialKey());
        // RequestBody를 DB에 Insert
        accountRepository.save(result);
    }
}
