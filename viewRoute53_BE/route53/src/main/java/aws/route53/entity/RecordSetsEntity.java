package aws.route53.entity;


import jdk.internal.jline.internal.Nullable;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tb_record_sets")
@Getter @Setter
public class RecordSetsEntity {
    @Id
    @GeneratedValue
    @Column(name = "record_sets_idx")
    private Integer recordSetsIdx;

    private String recordName;
    private String type;
    private Long expire;
    private String hostedZoneId;

    private String recordSetsValue;

    //== 생성 메서드 ==//
    public static RecordSetsEntity createRecordSets(String recordName,
                                                    String type,
                                                    Long expire,
                                                    String hostedZoneId,
                                                    String recordSetsValue) {
        RecordSetsEntity recordSets = new RecordSetsEntity();

        recordSets.setRecordName(recordName);
        recordSets.setType(type);
        recordSets.setExpire(expire);
        recordSets.setHostedZoneId(hostedZoneId);
        recordSets.setRecordSetsValue(recordSetsValue);

        return recordSets;
    }
}
