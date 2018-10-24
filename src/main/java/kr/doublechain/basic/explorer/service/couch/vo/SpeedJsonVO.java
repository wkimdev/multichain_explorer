package kr.doublechain.basic.explorer.service.couch.vo;


import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * SpeedJsonVO
 *
 */
@Getter
@Setter
public class SpeedJsonVO {
	
	/**
	 * 블록 넘버
	 */
	private BigInteger height;
	
	/**
	 * confirm check value
	 */
	private BigInteger checkConfirmNum;
	
	/**
	 * clue
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
	 * vihiclespeed
	 */
	private Integer vihiclespeed;
	
}
