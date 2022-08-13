package aws.route53.service;

import aws.route53.dto.AccountDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RecordSetsService recordSetsService;

    @Autowired
    AccountService accountService;

    @Autowired
    AwsCredentials awsCredentials;

    private String awsAccessKey = new String();
    private String awsCreateKey = new String();
    private String HostedZoneId = new String();

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        try {
            List<AccountDto> getAllAccounts = new ArrayList<>();
            getAllAccounts = accountService.getAllAccount();

            for (AccountDto getAccount : getAllAccounts) {
                recordSetsService.updateRecordSets(
                        getAccount.getHostedZoneId(),
                        getAccount.getAwsAccessKey(),
                        getAccount.getAwsCredentialKey()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
