package kr.doublechain.basic.explorer.service.couch.vo;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * FingerPrint StreamVO
 *
 */
@Getter
@Setter
@JsonIgnoreProperties({"chunks"})
public class FingerPrintVO {
		
	/**
	 * createtxid
	 */
	private String createtxid;
	
	/**
	 * confirm count
	 */
	private Integer confirmations;
	
	/**
	 * FingerPrint data
	 * 
	 */
	private FingerPringDataVO data;
	
	/**
	 * 블록 넘버
	 */
	private BigInteger height;
	
	/**
	 * name
	 */
	private String name;
	
	/**
	 * offchain
	 */
	private Boolean offchain;
	
	/**
	 * publishers
	 */
	private String publishers;
	
	/**
	 * key : stream 구분자 
	 */
	private String streamKeys;
	
	/**
	 * streamRef
	 */
	@JsonProperty("streamref")
	private String streamRef;
	
	/**
	 * time
	 */
	private Long time;
	
	/**
	 * txid
	 */
	private String txid;
	
	/**
	 * type
	 */
	private String type;
	
}
