package org.jbpm.spring;

import java.util.Map;



public interface JBPMServiceTaskExecutor {

	public Map<String, Object> execute(Map<String, Object> parameterMap) throws Exception;
}
