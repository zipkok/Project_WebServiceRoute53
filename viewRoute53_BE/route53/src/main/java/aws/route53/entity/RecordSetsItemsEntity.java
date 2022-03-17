package aws.route53.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_record_sets_items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordSetsItemsEntity {

    @Id
    @GeneratedValue
    @Column(name = "record_sets_items_idx")
    private Integer recordSetsItemsIdx;

    private String value;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_sets_idx")
    private RecordSetsEntity recordSets;

    //==생성 메서드==//
    public static RecordSetsItemsEntity createRecordSetsItems(String value) {
        RecordSetsItemsEntity recordSetsItem = new RecordSetsItemsEntity();
        recordSetsItem.setValue(value);
        return recordSetsItem;
    }

}
