package aws.route53.dto;

import aws.route53.entity.RecordSetsEntity;
import aws.route53.entity.RecordSetsItemsEntity;
import lombok.Data;

@Data
public class RecordSetsItemsDto {

    private String value;
    //private RecordSetsEntity recordSetsEntity;

    public RecordSetsItemsDto(RecordSetsItemsEntity recordSetsItemsEntity) {
        value = recordSetsItemsEntity.getValue();
        //recordSetsEntity = recordSetsItemsEntity.getRecordSets();
    }
}
