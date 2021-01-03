package com.thyng.web;

import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.tenant.TenantAware;
import com.thyng.domain.tenant.TenantContext;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class TenantAwareAdvice extends RequestBodyAdviceAdapter {

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		if(body instanceof TenantAware) {
			final TenantAware tenantAware = TenantAware.class.cast(body);
			final Tenant tenant = TenantContext.getTenant();
			if(null != tenant) {
				tenantAware.setTenantId(tenant.getId());
			}
		}
		return body;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

}
