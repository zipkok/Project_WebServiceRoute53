package aws.route53.dto;

import aws.route53.entity.RecordSetsEntity;
import lombok.Data;

@Data
public class RecordSetsDto {

    private String recordName;
    private String type;
    private Long expire;
    private String hostedZoneId;

    private String geoLocation;
    private String latencyLocation;

    private String recordSetsValue;

    // 생성자
    public RecordSetsDto() {}

    public RecordSetsDto(RecordSetsEntity recordSetsEntity) {
        recordName = recordSetsEntity.getRecordName();
        type = recordSetsEntity.getType();
        expire = recordSetsEntity.getExpire();
        hostedZoneId = recordSetsEntity.getHostedZoneId();
        recordSetsValue = recordSetsEntity.getRecordSetsValue();
    }
}
