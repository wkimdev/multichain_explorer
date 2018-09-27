package kr.doublechain.basic.explorer.service.couch.vo;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * client와 주고받을 모델
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FingerPrintWS {
	private String person;
	private String txid;		
	private String date;	
	private String state;	
	private Set<WebSocketSession> sessions = new HashSet<>();	

}
