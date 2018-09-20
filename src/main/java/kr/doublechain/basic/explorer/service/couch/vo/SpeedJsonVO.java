package kr.doublechain.basic.explorer.service.couch.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * SpeedJsonVO from SpeedDataVO
 *
 */
@Getter
@Setter
public class SpeedJsonVO {
	
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
