package kr.doublechain.basic.explorer.service.couch.vo;

import java.util.List;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonFormat;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

/**
 * SpeedDataResponse for lists
 *
 */
@Getter
@Setter
public class SpeedDataResponse {
	
	/**
	 * height
	 */
	private Integer height;
	
	/**
	 * overspeed
	 */
	private String overspeed;
	
	/**
	 * txid
	 */
	private String txid;
	
	
}