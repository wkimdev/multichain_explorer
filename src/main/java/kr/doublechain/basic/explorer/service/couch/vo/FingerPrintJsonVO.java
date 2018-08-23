package kr.doublechain.basic.explorer.service.couch.vo;

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
	 * image
	 *
	 */
	private String clue;
	
	/**
	 * date
	 *
	 */
	private String date;
	
	/**
	 * open
	 *
	 */
	private String open;
	
	/**
	 * who
	 *
	 */
	private String who;
	
}
