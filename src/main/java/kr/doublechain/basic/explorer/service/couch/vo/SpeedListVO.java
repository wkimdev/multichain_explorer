package kr.doublechain.basic.explorer.service.couch.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * SpeedListVO for lists
 *
 */
@Getter
@Setter
public class SpeedListVO {
	
	/**
	 * Latest Speed List
	 */
	private List<SpeedDataResponse> speedDataResponse;
	
	/**
	 * Speed Graph Data
	 */
	private List<SpeedCntResponse> dataResponse;
	
	/**
	 * SpeedCnt
	 */
	private Object speedCnt;
	
}