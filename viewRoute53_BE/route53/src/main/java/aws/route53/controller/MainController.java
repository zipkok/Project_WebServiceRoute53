package aws.route53.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class MainController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/", method= RequestMethod.GET)
    public String home() {
        return "Hello";
    }
}
