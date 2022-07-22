package aws.route53.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.route53.Route53Client;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsRequest;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsRequest.Builder;
import software.amazon.awssdk.services.route53.model.ListResourceRecordSetsResponse;
import software.amazon.awssdk.services.route53.model.ResourceRecordSet;

import java.util.ArrayList;
import java.util.List;

@Service
public class AwsCredentials {
    static Region AWS_REGION = Region.AWS_GLOBAL;

    private String awsAccessKey;
    private String awsSecretKey;

    private AwsBasicCredentials awsBasicCredentials;
    private Route53Client route53Client;

    public void setCredentials(String awsAccessKey, String awsSecretKey) {

        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;

        awsBasicCredentials = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
    }

    public void setRoute53Client() {
        this.route53Client = Route53Client
                        .builder()
                        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                        .region(AWS_REGION)
                        .build();
    }

    public List<ResourceRecordSet> getRoute53Info(String hostedZoneId) {
        List<ResourceRecordSet> allRecords = new ArrayList<>();
        String nextRecordName = null;

        do {
            Builder requestBuilder = ListResourceRecordSetsRequest.builder().hostedZoneId(hostedZoneId).maxItems("300");

            if (nextRecordName != null) {
                requestBuilder.startRecordName(nextRecordName);
            }
            ListResourceRecordSetsRequest request = requestBuilder.build();

            ListResourceRecordSetsResponse listResourceRecordSets = route53Client.listResourceRecordSets(request);
            nextRecordName = listResourceRecordSets.nextRecordName();
            allRecords.addAll(listResourceRecordSets.resourceRecordSets());

        } while (nextRecordName != null);

        route53Client.close();
        return allRecords;
    }
}
