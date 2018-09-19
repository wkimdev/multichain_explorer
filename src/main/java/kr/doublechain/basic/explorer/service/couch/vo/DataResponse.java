package kr.doublechain.basic.explorer.service.couch.vo;

import java.util.List;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonFormat;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

/**
 * FPrintListVO for lists
 *
 */
@Getter
@Setter
//@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class DataResponse {
	
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
	
	/**
	 * date
	 */
	private String date;
	
	/**
	 * status
	 */
	private String status;
	
	
}