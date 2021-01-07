package com.thyng;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.tenant.TenantContext;
import com.thyng.domain.tenant.TenantService;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevConfiguration implements WebMvcConfigurer {
	
	private final TenantService tenantService;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				final List<Tenant> tenants = tenantService.findAll();
				if(!tenants.isEmpty()) {
					TenantContext.setTenant(tenants.get(0));
				}
				return true;
			}
		});
	}
	
	

}
