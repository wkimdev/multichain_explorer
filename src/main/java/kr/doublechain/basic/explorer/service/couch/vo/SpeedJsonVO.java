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
	 * fine
	 */
	private String fine;
	
	/**
	 * location
	 */
	private String location;
	
	/**
	 * overspeed
	 */
	private Integer overspeed;
	
	/**
	 * vihiclespeed
	 */
	private Integer vihiclespeed;
	
}
