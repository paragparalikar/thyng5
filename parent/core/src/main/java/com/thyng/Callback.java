package com.thyng;

import java.util.function.Consumer;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@SuperBuilder
public class Callback<T> {

	private final Runnable after;
	private final Consumer<T> success;
	@Builder.Default
	private final Consumer<Throwable> failure = throwable -> log.error("", throwable);
	

	public void call(T item, Throwable throwable) {
		success(item, throwable);
		failure(item, throwable);	
		after();
	}
	
	protected void success(T item, Throwable throwable) {
		if (null != success && null == throwable) {
			try {
				success.accept(item);
			} catch (Throwable t) {
				log.error("Uncaught exception in success callback", t);
			}
		}
	}
	
	protected void failure(T item, Throwable throwable) {
		if (null != failure && null != throwable) {
			try {
				log.error("Error", throwable);
				failure.accept(throwable);
			} catch (Throwable t) {
				log.error("Uncaught exception in failure callback", t);
			}
		}
	}

	protected void after() {
		if (null != after) {
			try {
				after.run();
			} catch (Throwable t) {
				log.error("Uncaught exception in after callback", t);
			}
		}
	}
}
