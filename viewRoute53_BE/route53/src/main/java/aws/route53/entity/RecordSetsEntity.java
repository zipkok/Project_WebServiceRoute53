package aws.route53.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tb_record_sets")
public class RecordSetsEntity {
    @Id
    @GeneratedValue
    @Column(name = "record_sets_idx")
    private Integer recordSetsIdx;

    private String recordName;
    private String type;
    private String expire;

    @OneToMany(mappedBy = "recordSetsEntity", cascade = CascadeType.ALL)
    private List<RecordSetsItemsEntity> recordSetsItemsEntityList =  new ArrayList<>();
}
