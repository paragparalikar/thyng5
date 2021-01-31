package com.thyng.service.evaluation;

import java.util.EnumMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.thyng.domain.enumeration.Language;
import com.thyng.domain.intf.Lifecycle;

public class ScriptEvaluationService implements Lifecycle {
	
	private final ScriptEngineManager manager = new ScriptEngineManager();
	private final Map<Language, ScriptEngine> engines = new EnumMap<>(Language.class);

	@Override
	public void start() throws Exception {
		engines.put(Language.JAVASCRIPT, manager.getEngineByName("nashorn"));
	}
	
	public Object evaluate(String script, Language language, Map<String, Object> params) throws ScriptException {
		final ScriptEngine engine = engines.get(language);
		final Bindings bindings = engine.createBindings();
		bindings.putAll(params);
		return engine.eval(script, bindings);
	}
	
}
