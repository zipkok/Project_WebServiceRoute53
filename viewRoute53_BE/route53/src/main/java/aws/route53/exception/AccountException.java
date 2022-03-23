package aws.route53.exception;

import aws.route53.exception.AccountExcemption.ErrorCode;
import lombok.Getter;

@Getter
public class AccountException extends RuntimeException {

    private ErrorCode errorCode;

    public AccountException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
    
    // https://samtao.tistory.com/42 참고
}
