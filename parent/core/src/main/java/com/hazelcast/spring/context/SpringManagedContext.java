package com.hazelcast.spring.context;

import com.hazelcast.core.ManagedContext;
import com.hazelcast.executor.impl.RunnableAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringManagedContext implements ManagedContext, ApplicationContextAware {

    private AutowireCapableBeanFactory beanFactory;

	@Override
	@SuppressWarnings("rawtypes")
    public Object initialize(Object obj) {
        Object resultObject = obj;
        if (obj != null) {
            if (obj instanceof RunnableAdapter) {
                RunnableAdapter adapter = (RunnableAdapter) obj;
                Object runnable = adapter.getRunnable();
                runnable = initializeIfSpringAwareIsPresent(runnable);
                adapter.setRunnable((Runnable) runnable);
            } else {
                resultObject = initializeIfSpringAwareIsPresent(obj);
            }
        }
        return resultObject;
    }

	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private Object initializeIfSpringAwareIsPresent(Object obj) {
        Class clazz = obj.getClass();
        SpringAware s = (SpringAware) clazz.getAnnotation(SpringAware.class);
        Object resultObject = obj;
        if (s != null) {
            String name = s.beanName().trim();
            if (name.isEmpty()) {
                name = clazz.getName();
            }
            beanFactory.autowireBean(obj);
            resultObject = beanFactory.initializeBean(obj, name);
        }
        return resultObject;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }
}
