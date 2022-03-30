package aws.route53.dto;

import aws.route53.entity.RecordSetsEntity;
import lombok.Data;

@Data
public class CompareRecordSetsDto {
    private String recordName;
    private String type;
    private Long expire;

    private String recordSetsValue;

    private String routingPolicy;
    private String routeGeoLocation;
    private String routeLatencyRegion;
    private String routeWeight;


    // 생성자
    public CompareRecordSetsDto() {}

    public CompareRecordSetsDto(RecordSetsEntity recordSetsEntity) {
        recordName = recordSetsEntity.getRecordName();
        type = recordSetsEntity.getType();
        expire = recordSetsEntity.getExpire();
        recordSetsValue = recordSetsEntity.getRecordSetsValue();

        routingPolicy = recordSetsEntity.getRoutingPolicy();
        routeGeoLocation = recordSetsEntity.getRouteGeoLocation();
        routeLatencyRegion = recordSetsEntity.getRouteLatencyRegion();
        routeWeight = recordSetsEntity.getRouteWeight();

    }
}
