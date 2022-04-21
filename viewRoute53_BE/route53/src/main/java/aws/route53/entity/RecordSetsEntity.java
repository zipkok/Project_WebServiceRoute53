package aws.route53.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "tb_record_sets")
@Getter @Setter
public class RecordSetsEntity {
    @Id
    @GeneratedValue
    @Column(name = "record_sets_idx")
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

    //== 생성 메서드 ==//
    public static RecordSetsEntity createRecordSets(String recordName,
                                                    String type,
                                                    Long expire,
                                                    String hostedZoneId,
                                                    String recordSetsValue,
                                                    String routingPolicy,
                                                    String routingGeoLocation,
                                                    String routingLatencyRegion,
                                                    String routeWeight)
    {
        RecordSetsEntity recordSets = new RecordSetsEntity();

        recordSets.setRecordName(recordName);
        recordSets.setType(type);
        recordSets.setExpire(expire);
        recordSets.setHostedZoneId(hostedZoneId);
        recordSets.setRecordSetsValue(recordSetsValue);

        recordSets.setRoutingPolicy(routingPolicy);
        recordSets.setRouteGeoLocation(routingGeoLocation);
        recordSets.setRouteLatencyRegion(routingLatencyRegion);
        recordSets.setRouteWeight(routeWeight);

        return recordSets;
    }
}
