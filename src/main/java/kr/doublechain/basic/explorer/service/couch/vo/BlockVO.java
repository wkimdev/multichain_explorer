package kr.doublechain.basic.explorer.service.couch.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * BlockVO
 *
 */
@Getter
@Setter
public class BlockVO {
	
	/**
	 * bits
	 */
	private String bits;
	
	/**
	 * chainwork
	 */
	private String chainwork;
	
	/**
	 * 블록 컨펌 수
	 */
	private Integer confirmations;
	
	/**
	 * difficulty
	 */
	private BigDecimal difficulty;
	
	/**
	 * 블록 해시
	 */
	private String hash;
	
	/**
	 * 블록 넘버
	 */
	private BigInteger height;
	
	/**
	 * merkleroot
	 */
	private String merkleroot;
	
	/**
	 * miner
	 */
	private String miner;
	
	/**
	 * 다음 블록 해시
	 */
	private String nextblockhash;
	
	/**
	 * 이전 블록 해시
	 */
	private String previousblockhash;
	
	/**
	 * nonce
	 */
	private Integer nonce;
	
	/**
	 * size
	 */
	private Integer size;
	
	/**
	 * time
	 */
	private Timestamp time;
	
	/**
	 * tx
	 */
	private List<String> tx;
	
	/**
	 * version
	 */
	private Integer version;
	
}
