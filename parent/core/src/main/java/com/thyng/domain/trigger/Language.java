package com.thyng.domain.trigger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {
	
	SQL("SQL"), 
	JAVASCRIPT("Javascript"), 
	PYTHON("Python"), 
	JAVA("Java"), 
	GROOVY("Groovy"), 
	SCALA("Scala"), 
	RUBY("Ruby");
	
	private final String displayName;

}
