package com.thyng.web;

import org.eclipse.jetty.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.Callback;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Metric;
import com.thyng.domain.model.Sensor;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.dto.MetricDto;
import com.thyng.service.CacheService;
import com.thyng.service.MetricService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import spark.Request;
import spark.Response;
import spark.Spark;

@RequiredArgsConstructor
public class MetricController implements Lifecycle {
	
	@NonNull private final String path;
	@NonNull private final ObjectMapper objectMapper;
	@NonNull private final CacheService cacheService;
	@NonNull private final MetricService metricService;
	
	@Override
	public void start() throws Exception {
		Spark.post(path, this::handle);
	}
	
	private Object handle(Request request, Response response) {
		final MetricDto dto;
		try {
			 dto = objectMapper.readValue(request.body(), MetricDto.class);
		} catch(JsonProcessingException e) {
			response.status(HttpStatus.BAD_REQUEST_400);
			return null;
		}
		
		final Thing thing = cacheService.thing(dto.getThingId());
		final Template template = cacheService.template(thing.getTemplateId());
		final Metric metric = Metric.builder()
				.metricDto(dto)
				.thing(thing)
				.template(template)
				.build();
		dto.getValues().forEach((sensorId, value) -> {
			final Sensor sensor = template.sensor(sensorId);
			metric.getSensorValues().put(sensor, value);
		});
		metricService.create(metric, Callback.<Metric>builder().build());
		response.status(HttpStatus.OK_200);
		return null;
	}

}
