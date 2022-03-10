package aws.route53.dto;

import aws.route53.entity.AccountEntity;
import lombok.Data;

@Data
public class AccountDto {
    // Zone 이름, Zone Id, 계정이름, 팀이름
    private String hostedZoneName;
    private String hostedZoneId;
    private String accountName;
    private String team;

    public AccountDto(AccountEntity accountEntity) {
        hostedZoneName  = accountEntity.getHostedZoneName();
        hostedZoneId    = accountEntity.getHostedZoneId();

        accountName     = accountEntity.getAccountName();
        team            = accountEntity.getTeam();
    }
}
