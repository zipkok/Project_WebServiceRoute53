package aws.route53.controller;

import aws.route53.dto.testDto;
import aws.route53.entity.AccountEntity;
import aws.route53.service.AccountService;
import aws.route53.service.RecordSetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

    @RequestMapping(value = "/woobeom1", method= RequestMethod.GET)
    public String woobeom1(@RequestBody String body) {
        return "woobeom1: " + body;
    }

    @RequestMapping(value = "/woobeom2", method= RequestMethod.GET)
    public String woobeom2(@RequestBody String body) {
        return "woobeom2: " + body;
    }

    @RequestMapping(value = "/json/{vip_operation}", method= RequestMethod.POST)
    public HashMap woobeom_with_json(@PathVariable String vip_operation, @RequestBody testDto body) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("status", body.getStatus());
        map.put("hostname", body.getHostname());
        map.put("ipa", body.getIpa());
        return map;
    }
}
