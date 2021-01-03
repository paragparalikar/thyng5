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

import com.thyng.domain.trigger.Trigger;
import com.thyng.domain.trigger.TriggerService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/triggers")
public class TriggerController {

	@NonNull private final TriggerService triggerService;
	
	@GetMapping
	public List<Trigger> findAll(){
		return triggerService.findAll();
	}

	@GetMapping("/{id}")
	public Trigger getOne(@PathVariable final Integer id) {
		return triggerService.getOne(id);
	}
	
	@GetMapping(params = {"id","name"})
	public Boolean existsByName(@RequestParam final Integer id, @RequestParam final String name) {
		return triggerService.existsByName(id, name);
	}
	
	@PostMapping
	public Trigger create(@RequestBody @Valid @NonNull final Trigger trigger) {
		return triggerService.save(trigger);
	}
	
	@PutMapping
	public void update(@RequestBody @Valid @NonNull final Trigger trigger) {
		triggerService.save(trigger);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable final Integer id) {
		final Trigger trigger = triggerService.getOne(id);
		triggerService.delete(trigger);
	}
	
}
