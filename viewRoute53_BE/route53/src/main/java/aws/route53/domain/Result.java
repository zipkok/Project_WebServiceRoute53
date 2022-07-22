package aws.route53.domain;

import lombok.Getter;

@Getter
public enum Result {
    SUCCESS(0, "Success"),
    FAIL(-1, "Failed"),
    HTTP_REQUEST_ERROR(100, "HTTP Request Failed");

    private int code;
    private String msg;

    Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
