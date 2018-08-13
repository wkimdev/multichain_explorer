package kr.doublechain.basic.explorer.service.dcc.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

/**
 * Dcc BlockVO
 *
 */
@Getter
@Setter
public class DccVO {

	/**
	 * 트랜잭션 아이디
	 */
	private String txId;
	
	/**
	 * txId가 포함된 블록 해쉬
	 */
	private String blockHash;
	
	/**
	 * 코인 코드
	 */
	private String coinCode;
	
	/**
	 * 코인 지갑
	 */
	private String address;
	
	/**
	 * balance
	 */
	private BigDecimal balance;
	
	/**
	 * confirmations number
	 */
	private Integer confirmations;
	
	/**
	 * 해당 트랜잭션 정보를 업데이트한 시각
	 */
	private Timestamp updateDttm;
	
}
