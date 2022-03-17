package aws.route53.service;

import aws.route53.dto.AccountDto;
import aws.route53.entity.AccountEntity;
import aws.route53.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.route53.Route53Client;
import software.amazon.awssdk.services.route53.model.HostedZone;
import software.amazon.awssdk.services.route53.model.ListHostedZonesResponse;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public List<AccountEntity> getAccount() throws Exception {
        return accountRepository.findAllByOrderByAccountIdxDesc();
    }

    public void save(AccountDto accountDto) throws Exception {
//    public void save(AccountEntity accountEntity) throws Exception {
        // awscli 조회
//        List<HostedZone> awscliResult = ListHostedZones();
//        System.out.println("awscliResult: " + awscliResult);

        // 그 결과를 DB에 저장
        AccountEntity result = new AccountEntity();

        result.setAccountName(accountDto.getAccountName());
        result.setHostedZoneName(accountDto.getHostedZoneName());
        result.setHostedZoneId(accountDto.getHostedZoneId());
        result.setTeam(accountDto.getTeam());
        System.out.println("=========================================");
        System.out.println("result: " + result);

        // RequestBody를 DB에 Insert
        accountRepository.save(result);
    }

    /**
     * awscli 결과 조회
     */
    public List<HostedZone> ListHostedZones() throws Exception {
        // AWS Route53 Client
        Region region = Region.AWS_GLOBAL;
        Route53Client route53Client = Route53Client
                .builder()
                .region(region)
                // Access_Key, Credential_Key 입력하는 부분
                // .credentialsProvider('account_key', 'credential_key')
                .build();

        // aws route53 list-hosted-zones --profile
        ListHostedZonesResponse zonesResponse = route53Client.listHostedZones();
        List<HostedZone> checklist = zonesResponse.hostedZones();

        for (HostedZone check: checklist) {
           System.out.println("The name is : "+check.name());
        }

        route53Client.close();
        return checklist;
    }


    /*
     * 이미 존재하는 유저 체크
     */
//    private void validateDuplicateMember(Member member) {
//        // Exception
//        List<Member> findMembers = memberRepository.findByName(member.getName());
//        if (!findMembers.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        }
//    }
}
