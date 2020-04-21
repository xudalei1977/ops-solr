package com.leiyu.ops.solr.service.impl;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.leiyu.ops.solr.service.IpService;

@Service
public class IpServiceImpl implements IpService {
	private static Logger logger = Logger.getLogger(IpServiceImpl.class);
	@Value("${zk.host}")
	private String zkHost;
	@Value("${solr.collection.ip}")
	private String ipCollection;
	@Value("${zk.client.timeout}")
	private int zkClientTimeout;
	@Value("${zk.connect.timeout}")
	private int zkConnectTimeout;

	@Override
	public String getIpInfo(String ip) {
		CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
		cloudSolrServer.setDefaultCollection(ipCollection);
		cloudSolrServer.setZkConnectTimeout(zkConnectTimeout);
		cloudSolrServer.setZkClientTimeout(zkClientTimeout);
		cloudSolrServer.connect();
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		query.set("start", 0);
		query.set("rows", 1);
		query.set("sort", "id desc");
		query.addFilterQuery("minip:[* TO " + inet_aton(ip) + "]");
		query.addFilterQuery("maxip:[" + inet_aton(ip) + " TO *]");
		try {
			QueryResponse response = cloudSolrServer.query(query);
			SolrDocumentList docs = response.getResults();

			logger.info("文档个数：" + docs.getNumFound());
			logger.info("查询时间：" + response.getQTime());

			JSONArray arrJson = new JSONArray();
			for (SolrDocument doc : docs) {
				arrJson.add(doc);
			}
			String strResult = arrJson.toString();
			return strResult;
		} catch (Exception e) {
			logger.error("getIpInfo error :", e);
		}
		return null;
	}

	// 转化ip
	private static long inet_aton(String add) {
		long result = 0;
		// number between a dot
		long section = 0;
		// which digit in a number
		int times = 1;
		// which section
		int dots = 0;
		for (int i = add.length() - 1; i >= 0; --i) {
			if (add.charAt(i) == '.') {
				times = 1;
				section <<= dots * 8;
				result += section;
				section = 0;
				++dots;
			} else {
				section += (add.charAt(i) - '0') * times;
				times *= 10;
			}
		}
		section <<= dots * 8;
		result += section;
		return result;
	}
}
