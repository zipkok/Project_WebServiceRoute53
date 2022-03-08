package aws.route53.service;

import aws.route53.Entity.ListHostedZonesEntity;
import aws.route53.repository.ListHostedZonesRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.route53.Route53Client;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsRequest;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsResponse;
import software.amazon.awssdk.services.route53.model.ResourceRecordSet;
import software.amazon.awssdk.services.route53.model.Route53Exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListResourceRecordSets {


    @Autowired
    ListHostedZonesRepository listHostedZonesRepository;

    public List<ResourceRecordSet> ListResourceRecord(String requestHostedZoneId) {
        Region region = Region.AWS_GLOBAL;
        Route53Client route53Client = Route53Client.builder()
                .region(region)
                .build();

        // requestHostedZoneId을 Controller로 부터 받음.
        System.out.println("requestHostedZoneId = " + requestHostedZoneId);

        // listResourceRecord 호출
        List<ResourceRecordSet> result = listResourceRecord(route53Client, requestHostedZoneId);



        System.out.println("Result **** = " + result);


        route53Client.close();
        return result;
    }

    public List<ResourceRecordSet> listResourceRecord(Route53Client route53Client, String hostedZoneId) {
        List<ResourceRecordSet> result = new ArrayList<>();
        try {

            // HostZoneID를 사용하여 쿼리함.
            ListResourceRecordSetsRequest request = ListResourceRecordSetsRequest.builder()
                    .hostedZoneId(hostedZoneId)
                    .build();

            // 쿼리 결과
            ListResourceRecordSetsResponse listResourceRecordSets = route53Client.listResourceRecordSets(request);
            List<ResourceRecordSet> records = listResourceRecordSets.resourceRecordSets();

            for (ResourceRecordSet record : records) {
                result.add(record);
                System.out.println("The Record name is: " + record.name());
                System.out.println("The Record name is: " + record.regionAsString());
            }

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return result;
    }

}
