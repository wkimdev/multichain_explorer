package kr.doublechain.basic.explorer.service.couch.vo;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * FingerPrintJsonVO
 *
 */
@Getter
@Setter
public class FingerPrintJsonVO {
	
	/**
	 * 블록 넘버
	 */
	private BigInteger height;
	
	/**
	 * confirm check value
	 */
	private BigInteger checkConfirmNum;
	
	/**
	 * picture
	 */
	private String clue;
	
	/**
	 * date
	 */
	private String date;
	
	/**
	 * lat
	 */
	private String lat;
	
	/**
	 * lng
	 */
	private String lng;
	
	/**
	 * location
	 */
	private String location;
	
	/**
	 * person
	 */
	private String person;
	
	/**
	 * state
	 */
	private String state;
	
	
	
	
	
}
