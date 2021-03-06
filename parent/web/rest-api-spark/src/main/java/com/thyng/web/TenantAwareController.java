package com.thyng.web;

import org.eclipse.jetty.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.TenantAwareCrud;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.util.Throwables;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import spark.Request;
import spark.Spark;

@RequiredArgsConstructor
public class TenantAwareController<T extends TenantAwareModel<T>> implements Lifecycle {

	@NonNull private final String path;
	@NonNull private final Class<T> type;
	@NonNull private final ObjectMapper objectMapper;
	@NonNull private final TenantAwareCrud<T> repository;

	@Override
	public void start() throws Exception {
		Spark.path(path, () -> {
			Spark.get("", (request, response) -> repository.findAll(tenantId(request)));
			Spark.get("/", (request, response) -> repository.findAll(tenantId(request)));
			Spark.get("/:id", (request, response) -> repository.findById(tenantId(request), request.params(":id")));
			Spark.post("", (request, response) -> repository.save(transform(request)));
			Spark.post("/", (request, response) -> repository.save(transform(request)));
			Spark.put("", (request, response) -> repository.save(transform(request)));
			Spark.put("/", (request, response) -> repository.save(transform(request)));
			Spark.delete("/:id", (request, response) -> repository.deleteById(tenantId(request), request.params(":id")));
		});
	}
	
	private String tenantId(Request request) {
		return "1";
	}
	
	private T transform(Request request) {
		try {
			final T entity = objectMapper.readValue(request.body(), type);
			return entity.withTenantId(tenantId(request));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Spark.halt(HttpStatus.BAD_REQUEST_400, Throwables.stackTrace(e));
			return null;
		}
	}

}
