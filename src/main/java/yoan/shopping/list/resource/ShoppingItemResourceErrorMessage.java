package yoan.shopping.list.resource;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import yoan.shopping.infra.util.error.ErrorMessage;

/**
* @author yoan
*/
public enum ShoppingItemResourceErrorMessage implements ErrorMessage {
	/** Item not found */
	ITEM_NOT_FOUND("Item not found");

	private String message;
	
	private ShoppingItemResourceErrorMessage(String message) {
		checkArgument(isNotBlank(message), "An error message should not be empty");
		this.message = message;
	}
	
	@Override
	public String getDevReadableMessage() {
		return message;
	}

	@Override
	public String getDevReadableMessage(Object... params) {
		return String.format(message, params);
	}

}