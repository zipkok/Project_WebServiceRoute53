package aws.route53.Entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
@Table(name = "tb_account")
public class ListHostedZonesEntity {
    @Id
    @GeneratedValue
    public Integer accountIdx;

    private String hostZoneName;
    private String hostZoneId;
}
