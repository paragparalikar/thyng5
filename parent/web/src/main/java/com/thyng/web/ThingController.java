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

import com.thyng.domain.thing.Thing;
import com.thyng.domain.thing.ThingService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/things")
public class ThingController {

	@NonNull private final ThingService thingService;
	
	@GetMapping
	public List<Thing> findAll(){
		return thingService.findAll();
	}

	@GetMapping("/{id}")
	public Thing getOne(@PathVariable final Long id) {
		return thingService.getOne(id);
	}
	
	@GetMapping(params = {"id","name"})
	public Boolean existsByName(@RequestParam final Long id, @RequestParam final String name) {
		return thingService.existsByName(id, name);
	}
	
	@PostMapping
	public Thing create(@RequestBody @Valid @NonNull final Thing thing) {
		return thingService.save(thing);
	}
	
	@PutMapping
	public void update(@RequestBody @Valid @NonNull final Thing thing) {
		thingService.save(thing);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable final Long id) {
		final Thing thing = thingService.getOne(id);
		thingService.delete(thing);
	}
	
}
