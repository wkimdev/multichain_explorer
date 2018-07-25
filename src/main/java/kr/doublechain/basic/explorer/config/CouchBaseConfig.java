package kr.doublechain.basic.explorer.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchBaseConfig {
    @Value("${couchbase.bucket.name}")
    private String bucketName;

    @Value("${couchbase.bucket.user}")
    private String userName;

    @Value("${couchbase.bucket.password}")
    private String bucketPassword;

    @Value("${couchbase.bucket.ip}")
    private String bucketIp;

    @Autowired
    private Cluster couchbaseCluster;

    public @Bean Cluster cluster() {
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .connectTimeout(10000) //10000ms = 10s, default is 5s
                .build();
        System.setProperty("com.couchbase.queryEnabled", "true");
        return CouchbaseCluster.create(env, bucketIp);
    }

    public @Bean Bucket connectBucket() {
        couchbaseCluster.authenticate(userName, bucketPassword);
        return couchbaseCluster.openBucket(bucketName);
    }

}