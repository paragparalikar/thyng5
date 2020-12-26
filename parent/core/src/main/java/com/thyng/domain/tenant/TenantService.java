package com.thyng.domain.tenant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class TenantService {

	@Delegate
	private final TenantRepository tenantRepository;
	
}
