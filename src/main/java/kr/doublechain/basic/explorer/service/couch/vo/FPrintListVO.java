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
	
//	 private DataInnerVO data;
	private List<DataResponse> data;
	
	@Getter
	@Setter
	public static class DataResponse {
		
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
	
}