package kr.doublechain.basic.explorer.config;

import com.couchbase.client.java.Cluster;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchBaseConfig {
    
	@Value("${couchbase.bucket.ip}")
    private String bucketIp;

    @Value("${couchbase.bucket.user}")
    private String userName;

    @Value("${couchbase.bucket.password}")
    private String bucketPassword;
	
    @Bean("couchbaseCluster")
    public Cluster cluster() {
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .connectTimeout(30000) 
                .build();
        System.setProperty("com.couchbase.queryEnabled", "true");
        CouchbaseCluster couchbaseCluster = CouchbaseCluster.create(env, bucketIp);
		return couchbaseCluster.authenticate(userName, bucketPassword);
    }
	
}