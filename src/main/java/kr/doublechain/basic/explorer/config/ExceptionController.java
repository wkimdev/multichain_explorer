package kr.doublechain.basic.explorer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.common.vo.DccError;
import kr.doublechain.basic.explorer.common.vo.DccResponse;


/**
 * Controller Exception
 * created by basquiat
 *
 */
@ControllerAdvice
public class ExceptionController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);

	/**
	 * Bad Request Handler
	 * @param e
	 * @return ResponseEntity<?>
	 */
	@ExceptionHandler(value = {MethodArgumentTypeMismatchException.class,
							   TypeMismatchException.class,
							   IllegalArgumentException.class,
							   NullPointerException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public DccResponse<DccError> handle400(Exception e) {
		LOG.error("[ExceptionHandler-handle400] ", e);
		return CommonUtil.ResponseError(new DccError().setStatusCode(HttpStatus.BAD_REQUEST).setMessage("ExceptionHandler-handle400"));
	}
	
	/**
	 * Not Found Handler
	 * @param e
	 * @return ResponseEntity<?>
	 */
	@ExceptionHandler(value = {NoHandlerFoundException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public DccResponse<DccError> handle404(Exception e) {
		return CommonUtil.ResponseError(new DccError().setStatusCode(HttpStatus.NOT_FOUND).setMessage("You Request Wrong Url. See API Documents"));
	}
	
	/**
	 * Method Not Allowed Handler
	 * @param e
	 * @return ResponseEntity<?>
	 */
	@ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
	public DccResponse<DccError> handle405(Exception e) {
		LOG.error("[ExceptionHandler-handle405] ", e);
		return CommonUtil.ResponseError(new DccError().setStatusCode(HttpStatus.METHOD_NOT_ALLOWED).setMessage("ExceptionHandler-handle405"));
	}

	/**
	 * Internal Server Error Handler
	 * @param e
	 * @return ResponseEntity<?>
	 */
	@ExceptionHandler(value = {RuntimeException.class, Exception.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public DccResponse<DccError> handle500(Exception e) {
		LOG.info("################################################");
		LOG.info("###### exception: " + e.getLocalizedMessage() + " ######");
		LOG.info("################################################");
		LOG.error("[ExceptionHandler-handle500] ", e);
		return CommonUtil.ResponseError(new DccError().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR).setMessage("ExceptionHandler-handle500"));
	}

	/**
	 * status Handler
	 * @param e
	 * @return ResponseEntity<?>
	 */
//	@ExceptionHandler(value = {ApiException.class})
//	public DccResponse<DccError> handleApiException(ApiException e) {
//		LOG.error("[ExceptionHandler-ApiException] ", e);
//		return CommonUtil.ResponseError(new DccError().setStatusCode(e.getStatusCode()).setMessage("ExceptionHandler-ApiException"));
//	}

}
