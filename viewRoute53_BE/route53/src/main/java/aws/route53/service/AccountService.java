package aws.route53.service;

import aws.route53.dto.AccountDto;
import aws.route53.entity.AccountEntity;
import aws.route53.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public List<AccountEntity> getAccount() throws Exception {
        return accountRepository.findAllByOrderByAccountIdxDesc();
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

    /*
     * 이미 존재하는 유저 체크
     */
/*    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }*/
}
