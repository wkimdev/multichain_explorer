package kr.doublechain.basic.explorer.service.couch.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * FPrintListVO for lists
 *
 */
@Getter
@Setter
public class DataInnerVO {
	
	/**
	 * height
	 */
	private Integer height;
	
	/**
	 * person
	 */
	private String person;
	
	/**
	 * txid
	 */
	private String txid;
}
