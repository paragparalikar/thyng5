package com.thyng.domain.enumeration;

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
