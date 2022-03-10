package aws.route53.entity;

import lombok.*;

import javax.persistence.*;

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

    private String hostedZoneName;
    private String hostedZoneId;

    private String accountName;
    private String team;

    // 추후 서비스 할 예정.
    private String aws_secret_key;
    private String aws_access_key;
}