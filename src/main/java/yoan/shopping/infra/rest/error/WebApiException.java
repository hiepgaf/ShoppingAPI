/**
 * 
 */
package yoan.shopping.infra.rest.error;

import static java.util.Objects.requireNonNull;

import javax.ws.rs.core.Response.Status;

import yoan.shopping.infra.util.error.ApplicationException;
import yoan.shopping.infra.util.error.ErrorCode;
import yoan.shopping.infra.util.error.ErrorMessage;

/**
 * Exception at the web API level
 * Handled as HTTP response
 * @author yoan
 */
public class WebApiException extends ApplicationException {
	private static final long serialVersionUID = -7873617301665383085L;
	
	/** Status wanted in the response */
	private final Status status;
	
	public WebApiException(Status status, Level level, ErrorCode errorCode, String message) {
		super(level, errorCode, message);
		this.status = requireNonNull(status); 
	}

	public WebApiException(Status status, Level level, ErrorCode errorCode, String message, Throwable t) {
		super(level, errorCode, message, t);
		this.status = requireNonNull(status); 
	}
	
	public WebApiException(Status status, Level level, ErrorCode errorCode, ErrorMessage message) {
		super(level, errorCode, message);
		this.status = requireNonNull(status); 
	}
	
	public WebApiException(Status status, Level level, ErrorCode errorCode, ErrorMessage message, Throwable t) {
		super(level, errorCode, message, t);
		this.status = requireNonNull(status); 
	}

	public Status getStatus() {
		return status;
	}

}
