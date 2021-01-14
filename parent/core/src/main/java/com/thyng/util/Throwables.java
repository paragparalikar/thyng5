package com.thyng.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Throwables {

	public String stackTrace(Throwable throwable) {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final PrintStream printStream = new PrintStream(outputStream);
		throwable.printStackTrace(printStream);
		printStream.flush();
		return new String(outputStream.toByteArray());
	}
	
	
}
