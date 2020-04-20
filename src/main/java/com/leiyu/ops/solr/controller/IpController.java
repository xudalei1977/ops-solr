package com.leiyu.ops.solr.controller;

import com.leiyu.ops.solr.service.IpService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ip")
public class IpController {
    private static final Logger logger = Logger.getLogger(IpController.class);
    @Autowired
    private IpService ipService;

    @GetMapping(value = "/getIpInfo")
    public String getIpInfo(@RequestParam("ip") final String ip) throws Exception {
        final String ipInfo = this.ipService.getIpInfo(ip);
        return ipInfo;
    }
}
