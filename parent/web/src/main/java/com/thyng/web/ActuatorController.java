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

import com.thyng.domain.actuator.Actuator;
import com.thyng.domain.actuator.ActuatorService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/actuators")
public class ActuatorController {

	@NonNull private final ActuatorService actuatorService;
	
	@GetMapping
	public List<Actuator> findAll(){
		return actuatorService.findAll();
	}

	@GetMapping("/{id}")
	public Actuator getOne(@PathVariable final Integer id) {
		return actuatorService.getOne(id);
	}
	
	@GetMapping(params = {"id","name"})
	public Boolean existsByName(@RequestParam final Integer id, @RequestParam final String name) {
		return actuatorService.existsByName(id, name);
	}
	
	@PostMapping
	public Actuator create(@RequestBody @Valid @NonNull final Actuator actuator) {
		return actuatorService.save(actuator);
	}
	
	@PutMapping
	public void update(@RequestBody @Valid @NonNull final Actuator actuator) {
		actuatorService.save(actuator);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable final Integer id) {
		final Actuator actuator = actuatorService.getOne(id);
		actuatorService.delete(actuator);
	}
	
}
