package aws.route53;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class Route53Application {
	public static void main(String[] args) { SpringApplication.run(Route53Application.class, args);	}
}
