package kr.doublechain.basic.explorer.service.couch.vo;

import java.util.List;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonFormat;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonProperty;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

/**
 * FPrintListVO for lists
 *
 */
@Getter
@Setter
public class FPrintListVO {
	
	//@JsonDeserialize(as=FPrintListVO.class)
    //@JsonProperty("data")
	private List<DataResponse> dataResponse;
	
}