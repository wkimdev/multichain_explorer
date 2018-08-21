package kr.doublechain.basic.explorer.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * Common Response
 * 
 * @Setter(AccessLevel.NONE)
 * disable setter Options
 * 
 * @param <T>
 */
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
@Accessors(chain = true)
@ApiModel(description="Response")
public class DccResponse<T> {
	
	@ApiModelProperty(value = "header info", position = 1)
	private Header header; 
	
	@ApiModelProperty(value = "response data", position = 2)
	private T data;
	
	@ApiModelProperty(value = "response data", position = 2, hidden = true)
	private DccError error;
	
	@ApiModelProperty(value = "meta data", position = 3)
	private Meta meta;

}
