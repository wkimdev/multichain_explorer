package kr.doublechain.basic.explorer.service.couch.vo;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * VinVO from TransactionVO
 *
 */
@Getter
@Setter
public class VinVO {
	
	/**
	 * coinbase
	 */
	private String coinbase;
	
	/**
	 * sequence
	 */
	private BigInteger sequence;
	
}
