package kr.doublechain.basic.explorer.service.couch.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * FPrintListVO for lists
 * client와 websocket 으로 주고받을 모델
 * 
 */
@Getter
@Setter
public class DataResponse {
		
	/**
	 * person
	 */
	private String person;
	
	/**
	 * txid
	 */
	private String txid;
	
	/**
	 * date
	 */
	private String date;
	
	/**
	 * state
	 */
	private String state;
	
	
}