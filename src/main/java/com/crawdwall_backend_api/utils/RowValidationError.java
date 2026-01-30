package com.crawdwall_backend_api.utils;

public record RowValidationError(
		Integer row,
		String message		
	) {
}

