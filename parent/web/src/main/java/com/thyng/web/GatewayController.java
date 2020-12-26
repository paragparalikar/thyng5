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

import com.thyng.domain.gateway.Gateway;
import com.thyng.domain.gateway.GatewayService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/gateways")
public class GatewayController {

	@NonNull private final GatewayService gatewayService;
	
	@GetMapping
	public List<Gateway> findAll(){
		return gatewayService.findAll();
	}

	@GetMapping("/{id}")
	public Gateway getOne(@PathVariable final String id) {
		return gatewayService.getOne(id);
	}
	
	@GetMapping(params = {"id","name"})
	public Boolean existsByName(@RequestParam final String id, @RequestParam final String name) {
		return gatewayService.existsByName(id, name);
	}
	
	@PostMapping
	public Gateway create(@RequestBody @Valid @NonNull final Gateway gateway) {
		return gatewayService.save(gateway);
	}
	
	@PutMapping
	public void update(@RequestBody @Valid @NonNull final Gateway gateway) {
		gatewayService.save(gateway);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable final String id) {
		final Gateway gateway = gatewayService.getOne(id);
		gatewayService.delete(gateway);
	}
	
}
