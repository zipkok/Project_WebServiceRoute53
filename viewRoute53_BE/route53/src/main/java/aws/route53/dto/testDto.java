package aws.route53.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class testDto {
    private int id;
    private String status;
    private String hostname;
    private String ipa;

}
