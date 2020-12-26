package com.thyng.domain.action;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class ActionService {

	@Delegate
	private final ActionRepository actionRepository;
	
}
