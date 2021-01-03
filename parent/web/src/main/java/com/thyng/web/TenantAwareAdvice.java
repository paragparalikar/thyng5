package com.thyng.web;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.tenant.TenantAware;
import com.thyng.domain.tenant.TenantContext;
import com.thyng.domain.tenant.TenantService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class TenantAwareAdvice extends RequestBodyAdviceAdapter {
	
	
	// Testing - Remove this code
	private final TenantService tenantService;
	
	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		final Tenant tenant = tenantService.getOne("13d37e61-9065-4481-8799-a5ba47d66a93");
		TenantContext.setTenant(tenant);
		return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
	}
	
	// Testing

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		if(body instanceof TenantAware) {
			final TenantAware tenantAware = TenantAware.class.cast(body);
			tenantAware.setTenantId(TenantContext.getTenant().getId());
		}
		return body;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

}
