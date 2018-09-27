package kr.doublechain.basic.explorer.service.websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;

import kr.doublechain.basic.explorer.service.couch.vo.FingerPrintWS;
import lombok.Getter;

/**
 * 
 * 모델에서 받은 데이터를 send 시키는 액션 수행. ?
 * 
 */
@Getter
public class FingerPrintWSService {
	
	private String person;
	private String txid;		
	private String date;	
	private String state;
	private Set<WebSocketSession> sessions = new HashSet<>();
	
	public void handleMessage(WebSocketSession session, FingerPrintWS fingerPrintWS, ObjectMapper objectMapper) {        
        join(session);                    
        fingerPrintWS.setSessions(sessions);
        send(fingerPrintWS, objectMapper);
    }
	
	// ?
    private void join(WebSocketSession session) {
        sessions.add(session);
    }

    private <T> void send(FingerPrintWS fingerPrintWS, ObjectMapper objectMapper) {
    	// list를 어떻게 날리지?
        // TextMessage message = new TextMessage(objectMapper.);
        //sessions.parallelStream().forEach(session -> session.sendMessage(message));
    }
	
}
