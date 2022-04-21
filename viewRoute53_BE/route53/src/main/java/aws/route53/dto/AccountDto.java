package aws.route53.dto;

import aws.route53.entity.AccountEntity;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Data
public class AccountDto {
    // Zone 이름, Zone Id, 계정이름, 팀이름
    private Integer accountIdx;
    private String hostedZoneName;
    private String hostedZoneId;
    private String accountName;
    private String team;

    private String awsAccessKey;
    private String awsCredentialKey;

    //private Integer nslookupIDC;
    
    // 빈 생성자
    public AccountDto() {
        
    }
    
    public AccountDto(AccountEntity accountEntity) {
        accountIdx      = accountEntity.getAccountIdx();
        hostedZoneName  = accountEntity.getHostedZoneName();
        hostedZoneId    = accountEntity.getHostedZoneId();

        accountName     = accountEntity.getAccountName();
        team            = accountEntity.getTeam();

        awsAccessKey = accountEntity.getAws_access_key();
        awsCredentialKey = accountEntity.getAws_secret_key();

        // nslookupIDC = execNslookup();
    }

    public int execNslookup() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String test = "woobeom.com";
        processBuilder.command("cmd.exe", "/c", "nslookup " + test + " 8.8.8.8");
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with error COde: " + exitCode);
            return exitCode;

        } catch (IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
