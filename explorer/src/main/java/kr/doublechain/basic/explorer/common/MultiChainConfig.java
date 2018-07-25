package kr.doublechain.basic.explorer.common;

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
import org.springframework.stereotype.Component;

@Configuration
@Component
public class MultiChainConfig {
	
    private HttpClient client;
    
    private HttpPost request;

    public MultiChainConfig(@Value("${dcc.user}") final String user, 
    						@Value("${dcc.password}") final String password, 
    						@Value("${dcc.host}") final String host, 
    						@Value("${dcc.port}") final String port) {
    	CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		request = new HttpPost(CommonUtil.makeUrl(host, port));
    }

    public @Bean HttpClient client() {
        return this.client;
    }

    public @Bean HttpPost request() {
        return this.request;
    }
    
}