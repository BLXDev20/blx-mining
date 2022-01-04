package com.cc.blox.springconfig;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
	@Value("${httpclient.httpConnectionManager.maxTotalConnections}") private int maxTotalConnections;
	@Value("${httpclient.httpConnectionManager.defaultMaxConnectionsPerHost}") private int defaultMaxConnectionsPerHost;
	@Value("${httpclient.httpConnectionManager.soTimeout}") private int soTimeout;
	@Value("${httpclient.httpConnectionManager.connectionTimeout}") private int connectionTimeout;

	@Bean 
    public MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager() {
		MultiThreadedHttpConnectionManager connManager = new MultiThreadedHttpConnectionManager();
		connManager.getParams().setMaxTotalConnections(maxTotalConnections);
		connManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnectionsPerHost);
		connManager.getParams().setSoTimeout(soTimeout);
		connManager.getParams().setConnectionTimeout(connectionTimeout);
        return connManager; 
    }
	
	@Bean(name="httpClient")
    public HttpClient httpClient() { 
        return new HttpClient(multiThreadedHttpConnectionManager()); 
    }
}
