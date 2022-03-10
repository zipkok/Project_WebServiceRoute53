package aws.route53.service;

import aws.route53.entity.AccountEntity;
import aws.route53.entity.ListHostedZonesEntity;
import aws.route53.repository.AccountRepository;
import aws.route53.repository.ListHostedZonesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.route53.Route53Client;
import software.amazon.awssdk.services.route53.model.HostedZone;
import software.amazon.awssdk.services.route53.model.Route53Exception;

import java.util.*;


@Service
@RequiredArgsConstructor
public class ListHostedZones {

    @Autowired
    ListHostedZonesRepository listHostedZonesRepository;

    public List<HashMap<Object, Object>> ListHostedZones() {
        Region region = Region.AWS_GLOBAL;
        Route53Client route53Client = Route53Client.builder()
                .region(region)
                .build();

        List<HashMap<Object, Object>> list = listZones(route53Client);

        route53Client.close();
        return list;
    }

    @Autowired
    AccountRepository accountRepository;

    @Transactional
    public List<HashMap<Object, Object>> listZones(Route53Client route53Client) {
        List<HashMap<Object, Object>> list = new ArrayList<>();

        try {
            // AWSCLI 수행
            List<HostedZone> checklist = route53Client.listHostedZones().hostedZones();

            // 이미 존재하는 데이터 Select
            String existDomainName = accountRepository.findAllByOrderByAccountIdxDesc().toString();

            // 값을 비교할 때 사용할 toString 데이터
            for (HostedZone result: checklist) {
                // 값이 존재하지 않을 때 -> Insert
                if(!existDomainName.contains(result.name())) {
                    AccountEntity insertData = new AccountEntity();
                    
                    insertData.setHostedZoneId(result.id());
                    insertData.setHostedZoneName(result.name());
                    insertData.setAccountName("AccountName");
                    insertData.setTeam("Team");
                    
                    accountRepository.save(insertData);

                    // ListHostedZonesEntity insertData = new ListHostedZonesEntity();
                    // insertData.setHostZoneId(result.id());
                    // insertData.setHostZoneName(result.name());
                    // listHostedZonesRepository.save(insertData);

                    System.out.println("============================================");
                }
            }

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return list;
    }

    public List<HashMap<Object, Object>> testZones(Route53Client route53Client) {
        List<HashMap<Object, Object>> list = new ArrayList<>();

        try {
            List<HostedZone> checklist = route53Client.listHostedZones().hostedZones();
            System.out.println(checklist);
            for (HostedZone check : checklist) {

                HashMap<Object, Object> map = new HashMap<>();

                map.put("HostZoneName", check.name());
                map.put("HostZoneId", check.id());
                list.add(map);
            }

        } catch (Route53Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return list;
    }
}