package kr.doublechain.basic.explorer.service.couch.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * FPDataVO from FingerPrint StreamVO
 *
 */
@Getter
@Setter
public class FingerPrintDataVO {
	
	/**
	 * finiger stream의 jsonObject data
	 *
	 */
	private FingerPrintJsonVO json;

}
