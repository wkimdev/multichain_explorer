package kr.doublechain.basic.explorer.service.couch.vo;

import java.util.List;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonFormat;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

/**
 * SpeedListVO for lists
 *
 */
@Getter
@Setter
public class SpeedListVO {
	
	private List<SpeedDataResponse> speedDataResponse;
	
}