package com.thyng.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.tenant.TenantService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/tenants")
public class TenantController {

	private final TenantService tenantService;
	
	@GetMapping
	public List<Tenant> findAll(){
		return tenantService.findAll();
	}

	@GetMapping("/{id}")
	public Tenant getOne(@PathVariable final Integer id) {
		return tenantService.getOne(id);
	}
	
	@GetMapping(params = {"id","name"})
	public Boolean existsByName(@RequestParam final Integer id, @RequestParam final String name) {
		return tenantService.existsByName(id, name);
	}
	
	@PostMapping
	public Tenant create(@RequestBody @Valid @NonNull final Tenant tenant) {
		final Tenant managed = tenantService.save(tenant);
		return managed;
	}
	
	@PutMapping
	public void update(@RequestBody @Valid @NonNull final Tenant tenant) {
		tenantService.save(tenant);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable final Integer id) {
		final Tenant tenant = tenantService.getOne(id);
		tenantService.delete(tenant);
	}
	
}
