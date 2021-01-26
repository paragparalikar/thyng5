package com.thyng.web;

import org.eclipse.jetty.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.domain.intf.Crud;
import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Nameable;
import com.thyng.util.Throwables;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import spark.Request;
import spark.Spark;

@RequiredArgsConstructor
public class Controller<T extends Identifiable<T, String> & Nameable> implements Lifecycle {

	@NonNull private final String path;
	@NonNull private final Class<T> type;
	@NonNull private final ObjectMapper objectMapper;
	@NonNull private final Crud<T, String> repository;

	@Override
	public void start() throws Exception {
		Spark.path(path, () -> {
			Spark.get("", (request, response) -> repository.findAll());
			Spark.get("/", (request, response) -> repository.findAll());
			Spark.get("/:id", (request, response) -> repository.findById(request.params(":id")));
			Spark.post("", (request, response) -> repository.save(transform(request)));
			Spark.post("/", (request, response) -> repository.save(transform(request)));
			Spark.put("", (request, response) -> repository.save(transform(request)));
			Spark.put("/", (request, response) -> repository.save(transform(request)));
			Spark.delete("/:id", (request, response) -> repository.deleteById(request.params(":id")));
		});
	}
	
	private T transform(Request request) {
		try {
			return objectMapper.readValue(request.body(), type);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Spark.halt(HttpStatus.BAD_REQUEST_400, Throwables.stackTrace(e));
			return null;
		}
	}

}
