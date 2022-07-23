package aws.route53.controller;

import aws.route53.service.AccountService;
import aws.route53.service.RecordSetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class MainController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AccountService accountService;

    @Autowired
    RecordSetsService recordSetsService;

    @RequestMapping(value = "/", method= RequestMethod.GET)
    public String home() {
        return "Hello";
    }
}
