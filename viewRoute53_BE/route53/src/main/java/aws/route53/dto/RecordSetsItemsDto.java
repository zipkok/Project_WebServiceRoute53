package aws.route53.dto;

import aws.route53.entity.RecordSetsEntity;
import aws.route53.entity.RecordSetsItemsEntity;

public class RecordSetsItemsDto {

    private String value;
    private RecordSetsEntity recordSetsEntity;

    public RecordSetsItemsDto(RecordSetsItemsEntity recordSetsItemsEntity) {
        value = null;
        recordSetsEntity = null;
    }
}
