package com.lavapm.solr.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lavapm.solr.service.IpService;

@RestController
@RequestMapping(value = "/ip")
public class IpController {
	@Autowired
	private IpService ipService;
	private static Logger logger = Logger.getLogger(IpController.class);

	@GetMapping(value = "/getIpInfo")
	public String getIpInfo(@RequestParam("ip") String ip) throws Exception {
		String ipInfo = ipService.getIpInfo(ip);
		return ipInfo;
	}
}
