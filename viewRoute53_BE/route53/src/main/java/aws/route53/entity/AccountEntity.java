package aws.route53.entity;

import lombok.*;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
@Table(name = "tb_account")
public class AccountEntity {
    @Id
    @GeneratedValue
    @Column(name = "account_idx")
    public Integer accountIdx;


    private String hostedZoneId;
    private String hostedZoneName;


    private String accountName;
    private String team;

    // 추후 서비스 할 예정.
    private String aws_secret_key;
    private String aws_access_key;

    public static AccountEntity createAccount(String hostedZoneName,
                                              String hostedZoneId,
                                              String accountName,
                                              String team,
                                              String aws_access_key,
                                              String aws_secret_key) {
        AccountEntity account = new AccountEntity();

        account.setHostedZoneName(hostedZoneName);
        account.setHostedZoneId(hostedZoneId);
        account.setAccountName(accountName);
        account.setTeam(team);
        account.setAws_access_key(aws_access_key);
        account.setAws_secret_key(aws_secret_key);

        return account;
    }

    public static AccountEntity updateAccount(Integer accountIdx,
                                              String hostedZoneName,
                                              String hostedZoneId,
                                              String accountName,
                                              String team,
                                              String aws_access_key,
                                              String aws_secret_key) {
        AccountEntity account = new AccountEntity();

        account.setAccountIdx(accountIdx);
        account.setHostedZoneName(hostedZoneName);
        account.setHostedZoneId(hostedZoneId);
        account.setAccountName(accountName);
        account.setTeam(team);
        account.setAws_access_key(aws_access_key);
        account.setAws_secret_key(aws_secret_key);

        return account;
    }
}