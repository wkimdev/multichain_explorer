package kr.doublechain.basic.explorer.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * 
 * WebSocketConfig
 * 
 * created by basquiat
 *
 */
@EnableWebSocketMessageBroker
@Configuration
@ComponentScan
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
	@Value("${websocket.channel}")
	private String WEBSOCKET_ENDPOINT; ///ws
	
	@Value("${websocket.subscribe.channel}")
	private String BROADCAST_CHANNEL;
	
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
		System.out.println(">>>>>>>>> this is end point");
        registry.addEndpoint(WEBSOCKET_ENDPOINT).setAllowedOrigins("*").withSockJS();
    }
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(BROADCAST_CHANNEL);
    }
	  
//	  @Override
//	  public void configureMessageBroker(MessageBrokerRegistry registry) {
//		  registry.enableSimpleBroker("/topic");
//		  registry.setApplicationDestinationPrefixes("/app");
//	  }
//	
//	  @Override
//	  public void registerStompEndpoints(StompEndpointRegistry registry) {
//	      registry.addEndpoint("/socketHandler").withSockJS();
//	  }
 
}
