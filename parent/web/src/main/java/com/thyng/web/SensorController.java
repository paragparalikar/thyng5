package com.thyng.web;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

import com.thyng.domain.sensor.Sensor;
import com.thyng.domain.sensor.SensorService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/sensors")
public class SensorController {

	@NonNull private final SensorService sensorService;
	
	@GetMapping
	public List<Sensor> findAll(){
		return sensorService.findAll();
	}

	@GetMapping(params="thingId")
	public List<Sensor> findByThingId(@RequestParam @NotBlank final String thingId){
		return sensorService.findByThingId(thingId);
	}
	
	@GetMapping("/{id}")
	public Sensor getOne(@PathVariable final String id) {
		return sensorService.getOne(id);
	}
	
	@GetMapping(params = {"id","name"})
	public Boolean existsByName(@RequestParam final String id, @RequestParam final String name) {
		return sensorService.existsByName(id, name);
	}
	
	@PostMapping
	public Sensor create(@RequestBody @Valid @NonNull final Sensor sensor) {
		return sensorService.save(sensor);
	}
	
	@PutMapping
	public void update(@RequestBody @Valid @NonNull final Sensor sensor) {
		sensorService.save(sensor);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable final String id) {
		final Sensor sensor = sensorService.getOne(id);
		sensorService.delete(sensor);
	}
	
}
