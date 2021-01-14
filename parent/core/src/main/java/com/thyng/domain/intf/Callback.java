package com.thyng.domain.intf;

import java.util.function.Consumer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Callback<T> {

	private final Consumer<T> success;
	private final Consumer<Throwable> failure;
	private final Runnable after;
	
	public void after(T item, Throwable throwable) {
		if(null != success && null == throwable) try { success.accept(item); } catch(Throwable t) {};
		if(null != failure && null != throwable) try { failure.accept(throwable); } catch(Throwable t) {};
		if(null != after) try { after.run(); } catch(Throwable t) {};
	}
	
}
