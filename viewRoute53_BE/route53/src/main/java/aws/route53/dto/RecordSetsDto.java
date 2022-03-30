package aws.route53.dto;

import aws.route53.entity.RecordSetsEntity;
import lombok.Data;

@Data
public class RecordSetsDto {
    private Integer recordSetsIdx;

    private String recordName;
    private String type;
    private Long expire;
    private String hostedZoneId;

    private String recordSetsValue;

    private String routingPolicy;
    private String routeGeoLocation;
    private String routeLatencyRegion;
    private String routeWeight;


    // 생성자
    public RecordSetsDto() {}

    public RecordSetsDto(RecordSetsEntity recordSetsEntity) {
        recordSetsIdx = recordSetsEntity.getRecordSetsIdx();

        recordName = recordSetsEntity.getRecordName();
        type = recordSetsEntity.getType();
        expire = recordSetsEntity.getExpire();
        hostedZoneId = recordSetsEntity.getHostedZoneId();

        recordSetsValue = recordSetsEntity.getRecordSetsValue();

        routingPolicy = recordSetsEntity.getRoutingPolicy();
        routeGeoLocation = recordSetsEntity.getRouteGeoLocation();
        routeLatencyRegion = recordSetsEntity.getRouteLatencyRegion();
        routeWeight = recordSetsEntity.getRouteWeight();

    }
}
