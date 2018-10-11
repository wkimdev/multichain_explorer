package kr.doublechain.basic.explorer.service.couch.vo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * FPrintListVO for lists
 *
 */
@Getter
@Setter
public class FPrintListVO {
	
	private List<DataResponse> dataResponse;
	
	/**
	 * Door Access Graph Data
	 */
	private List<FingerPrintCntResponse> dataResponseGraph;
	
	/**
	 * DoorAccessCnt
	 */
	private Object DoorAccessCnt;
	
	/**
	 * call check
	 */
	String message;
}