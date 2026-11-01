package com.example.vsd.frontend.model;

import lombok.Builder;

import java.util.List;

@Builder
public record LoginResult(
		String accessToken,
		Long expiresIn,
		List<String> authorities
) {
}
