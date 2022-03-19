package aws.route53.dto;

import aws.route53.entity.RecordSetsEntity;
import aws.route53.entity.RecordSetsItemsEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecordSetsDto {

    private String recordName;
    private String type;
    private Long expire;
    private String hostedZoneId;

    private String geoLocation;
    private String latencyLocation;

    // private List<RecordSetsItemsDto> recordSetsItems;
    private String recordSetsItems;

    // 생성자
    public RecordSetsDto() {}

    public RecordSetsDto(RecordSetsEntity recordSetsEntity) {
        recordName = recordSetsEntity.getRecordName();
        type = recordSetsEntity.getType();
        expire = recordSetsEntity.getExpire();
        hostedZoneId = recordSetsEntity.getHostedZoneId();

        recordSetsItems = recordSetsEntity
                .getRecordSetsItems()
                .stream()
                .map(recordSetsItems -> new RecordSetsItemsDto(recordSetsItems))
                .collect(Collectors.toList())
                .toString()
                .replaceAll("\\[RecordSetsItemsDto", "")
                .replaceAll("RecordSetsItemsDto", "")
                .replaceAll("(Value=)","")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("]", "")
                .replaceAll("value=", "");
    }
}
