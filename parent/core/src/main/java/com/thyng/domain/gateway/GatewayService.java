package com.thyng.domain.gateway;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class GatewayService {

	@Delegate
	private final GatewayRepository gatewayRepository;

} 
