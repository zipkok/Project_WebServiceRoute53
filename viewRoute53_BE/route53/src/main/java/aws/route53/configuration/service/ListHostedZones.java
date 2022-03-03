package aws.route53.configuration.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.route53.Route53Client;
import software.amazon.awssdk.services.route53.model.HostedZone;
import software.amazon.awssdk.services.route53.model.Route53Exception;
import software.amazon.awssdk.services.route53.model.ListHostedZonesResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ListHostedZones {

    public HashMap<String,String> ListHostedZones() {
        Region region = Region.AWS_GLOBAL;
        Route53Client route53Client = Route53Client.builder()
                .region(region)
                .build();

        HashMap<String,String> list = listZones(route53Client);

        route53Client.close();
        return list ;
    }

    public HashMap<String,String> listZones(Route53Client route53Client) {
        List<String> list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<String, String>();

        try {
            List<HostedZone> checklist = route53Client.listHostedZones().hostedZones();

            for (HostedZone check: checklist) {
                list.add("data");
                System.out.println(check);
                map.put("HostZoneName", check.name());
                map.put("HostZoneId", check.id());
            }

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        // return list;
        return map;
    }
}
