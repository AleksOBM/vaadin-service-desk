package com.example.vsd.manager.enity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LastModifiedByListener {

	@PrePersist
	@PreUpdate
	public void setLastModifiedBy(BaseEntity entity) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null
				|| !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken) {
			throw new IllegalStateException("Authenticated user is required to modify an entity");
		}
		entity.setLastModifiedBy(authentication.getName());
	}
}
