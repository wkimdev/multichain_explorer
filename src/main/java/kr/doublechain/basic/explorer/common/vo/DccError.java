package kr.doublechain.basic.explorer.common.vo;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * Common DccError
 *
 * @param <T>
 */
@Setter
@Getter
@Accessors(chain = true)
public class DccError {
	
	private HttpStatus statusCode;
	
	private String message;
				
}
