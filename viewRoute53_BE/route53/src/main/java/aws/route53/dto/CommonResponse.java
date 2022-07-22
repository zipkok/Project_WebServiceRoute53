package aws.route53.dto;

import aws.route53.domain.Result;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonSerializable;
import io.swagger.models.auth.In;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public CommonResponse(Result resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public CommonResponse(Result resultEnum, T result) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.result = result;
    }
}
