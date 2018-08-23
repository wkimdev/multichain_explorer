package kr.doublechain.basic.explorer.common.vo;

import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel(description="Header")
public class Header {
	
	@ApiModelProperty(value = "code")
	private Integer code;
	
	@ApiModelProperty(value = "message")
	private String message = "";
	
	@ApiModelProperty(value = "request time")
	private String requestTime = CommonUtil.makeCurrentDateCustomFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	@ApiModelProperty(value = "response time")
	private String responseTime = CommonUtil.makeCurrentDateCustomFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
}
