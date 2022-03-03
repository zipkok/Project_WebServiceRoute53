/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/
package aws.route53;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.route53.Route53Client;
import software.amazon.awssdk.services.route53.model.HostedZone;
import software.amazon.awssdk.services.route53.model.Route53Exception;
import software.amazon.awssdk.services.route53.model.ListHostedZonesResponse;
import java.util.List;

//@Component
//@RequiredArgsConstructor
public class ListHostedZones {


//    public static void ListHostedZones() {
//        Region region = Region.AWS_GLOBAL;
//        Route53Client route53Client = Route53Client.builder()
//                .region(region)
//                .build();
//
//        listZones(route53Client);
//        route53Client.close();
//    }
//
//    public static void listZones(Route53Client route53Client) {
//
//        try {
//
//            ListHostedZonesResponse zonesResponse = route53Client.listHostedZones();
//            List<HostedZone> checklist = zonesResponse.hostedZones();
//
//            for (HostedZone check: checklist) {
//                System.out.println("The name is : "+check.name());
//            }
//
//        } catch (Route53Exception e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//    }
}