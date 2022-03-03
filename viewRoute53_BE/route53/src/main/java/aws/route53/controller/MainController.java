package aws.route53.controller;

import aws.route53.configuration.service.ListHostedZones;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
public class MainController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ListHostedZones listHostedZones;

    @RequestMapping(value = "/", method= RequestMethod.GET)
    public String home() {

        return "Hello";
    }

    @RequestMapping(value = "/list", method= RequestMethod.GET)
    public HashMap<String,String> list() {
        return listHostedZones.ListHostedZones();
    }

}
