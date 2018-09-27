package kr.doublechain.basic.explorer.service.couch.vo;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;

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
	 * vihiclespeed
	 */
	@JsonIgnore
	@JsonProperty(value = "vihiclespeed")
	private String vihiclespeed;
	
	/**
	 * txid
	 */
	private String txid;
	
	/**
	 * location
	 */
	private String location;
	
	/**
	 * date
	 */
	private String date;
	
	
}