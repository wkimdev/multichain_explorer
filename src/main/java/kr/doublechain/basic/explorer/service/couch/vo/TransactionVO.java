package kr.doublechain.basic.explorer.service.couch.vo;

import java.math.BigInteger;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * TransactionVO
 *
 */
@Getter
@Setter
public class TransactionVO {
	
	/**
	 * 블록 해시
	 */
	private String blockhash;
	
	/**
	 * blocktime
	 */
	private Timestamp blocktime;
	
	/**
	 * 컨펌수
	 */
	private Integer confirmations;
	
	/**
	 * 블록 넘버
	 */
	private BigInteger height;
	
	/**
	 * lock time
	 */
	private Timestamp locktime;
	
	/**
	 * time
	 */
	private Timestamp time;
	
	/**
	 * 트랜잭션id
	 */
	private String txid;
	
	/**
	 * version
	 */
	private Integer version;
	
	/**
	 * vin
	 */
	private VinVO vin;
	
	/**
	 * vout
	 */
	private VoutVO vout;
	
}
