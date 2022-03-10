package aws.route53.dto;

import aws.route53.entity.RecordSetsEntity;
import aws.route53.entity.RecordSetsItemsEntity;
import lombok.Data;

import java.util.List;

@Data
public class RecordSetsDto {

    private String recordName;
    private String type;
    private String expire;
    private List<RecordSetsItemsEntity> recordSetsItemsEntityList;

    public RecordSetsDto(RecordSetsEntity recordSetsEntity) {
        recordName = null;
        type = null;
        expire = null;
        recordSetsItemsEntityList = null;
    }
}
