package kr.doublechain.basic.explorer.service.couch.vo;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * FPDataVO from FingerPrint StreamVO
 *
 */
@Getter
@Setter
public class FingerPrintJsonVO {
	
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
