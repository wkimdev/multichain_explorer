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
	private String picture;
	
	/**
	 * person
	 */
	private String person;
	
	/**
	 * open
	 */
	private String open;
	
	/**
	 * location
	 */
	private String location;
	
	/**
	 * date
	 */
	private String date;
	
}
