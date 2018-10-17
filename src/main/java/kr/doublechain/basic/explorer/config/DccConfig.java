package kr.doublechain.basic.explorer.config;

import com.couchbase.client.java.Cluster;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DccConfig {
    
	@Value("${dcc.host}")
    private String host;
    
    @Value("${dcc.port}")
    private String port;
    
    @Value("${dcc.user}")
    private String user;
    
    @Value("${dcc.password}")
    private String password;
    
    private HttpClient httpClient;

    private HttpPost httpPost;
    
    public DccConfig(@Value("${dcc.host}") final String host,
                     @Value("${dcc.port}") final String port,
                     @Value("${dcc.user}") final String user,
                     @Value("${dcc.password}") final String password
                      ) {
        
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        httpPost = new HttpPost(kr.doublechain.basic.explorer.common.utils.CommonUtil.makeUrl(host, port));
    }
    
    /**
     * @return HttpClient
     */
    public @Bean HttpClient httpClient() {
        return this.httpClient;
    }
    
    /**
     * @return httpPost
     */
    public @Bean HttpPost httpPost() {
        return this.httpPost;
    }
	
}