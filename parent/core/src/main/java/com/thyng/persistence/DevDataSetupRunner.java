package com.thyng.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.tenant.TenantRepository;

import lombok.RequiredArgsConstructor;

//@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataSetupRunner implements CommandLineRunner {

	private final TenantRepository tenantRepository;

	@Override
	public void run(String... args) throws Exception {
		final List<Tenant> tenants = tenants();
		tenants.forEach(tenantRepository::save);
	}

	private List<Tenant> tenants(){
		final List<Tenant> tenants = new ArrayList<>(10);
		for(int index = 0; index < 10; index++) {
			final Tenant tenant = new Tenant();
			tenant.setName("Tenant_" + index);
			tenants.add(tenant);
		}
		return tenants;
	}
	
}
