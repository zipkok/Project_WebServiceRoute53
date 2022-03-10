package aws.route53.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tb_record_sets_items")
public class RecordSetsItemsEntity {

    @Id
    @GeneratedValue
    @Column(name = "record_sets_items_idx")
    private Integer recordSetsItemsIdx;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_sets_idx")
    private RecordSetsEntity recordSetsEntity;
}
