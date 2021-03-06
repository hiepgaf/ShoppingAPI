package io.tyoras.shopping.authentication.realm;

import java.security.Principal;
import java.util.UUID;

import io.tyoras.shopping.user.User;

public final class BasicUserPrincipal implements Principal {
	
	private final UUID userId;
	
	public BasicUserPrincipal(User user) {
		userId = user.getId();
	}

	@Override
	public String getName() {
		return userId.toString();
	}

	public UUID getUserId() {
		return userId;
	}

	
}
