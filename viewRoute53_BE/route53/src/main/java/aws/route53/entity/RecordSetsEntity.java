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

    // Only Print a Latency Based
    @Nullable
    private String latencyLocation;

    @OneToMany(mappedBy = "recordSets", cascade = CascadeType.ALL)
    private List<RecordSetsItemsEntity> recordSetsItems =  new ArrayList<>();

    public void addRecordSetsItem(RecordSetsItemsEntity recordSetsItemsEntity) {
        recordSetsItems.add(recordSetsItemsEntity);
        recordSetsItemsEntity.setRecordSets(this);
    }

    //== 생성 메서드 ==//
    public static RecordSetsEntity createRecordSets(String recordName,
                                                    String type,
                                                    Long expire,
                                                    String hostedZoneId,
                                                    RecordSetsItemsEntity... recordSetsItems) {
        RecordSetsEntity recordSets = new RecordSetsEntity();

        recordSets.setRecordName(recordName);
        recordSets.setType(type);
        recordSets.setExpire(expire);
        recordSets.setHostedZoneId(hostedZoneId);

        for (RecordSetsItemsEntity recordSetsItem : recordSetsItems) {
            recordSets.addRecordSetsItem(recordSetsItem);
        }
        return recordSets;
    }
}
