package org.jbpm.spring;

import java.util.List;
import java.util.Map;

import org.jbpm.bpmn2.handler.SignallingTaskHandlerDecorator;
import org.jbpm.runtime.manager.impl.DefaultRegisterableItemsFactory;
import org.jbpm.services.task.audit.JPATaskLifeCycleEventListener;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.task.TaskLifeCycleEventListener;
import org.springframework.beans.factory.annotation.Autowired;




public class JBPMRegisterableItemsFactory extends DefaultRegisterableItemsFactory {

	@Autowired
	private JBPMServiceTaskHandler serviceTaskHandler;
	
	
	@Override
	public List<TaskLifeCycleEventListener> getTaskListeners() {
		List<TaskLifeCycleEventListener> listeners = super.getTaskListeners();	
		listeners.add(new JPATaskLifeCycleEventListener(true));							
		return listeners;
	}
	
	@Override
	public List<ProcessEventListener>  getProcessEventListeners(RuntimeEngine runtime) {
		List<ProcessEventListener> listeners = super.getProcessEventListeners(runtime);
		listeners.add(new JBPMProcessEventListener());
		
		return listeners;
	}
	
	@Override
	public Map<String, WorkItemHandler> getWorkItemHandlers(RuntimeEngine runtime) {
		Map<String, WorkItemHandler> handlers = super.getWorkItemHandlers(runtime);
		
		String eventType = "Error-code";
		SignallingTaskHandlerDecorator signallingTaskWrapper = new SignallingTaskHandlerDecorator(serviceTaskHandler, eventType);
		
		handlers.put("Service Task", signallingTaskWrapper);
		
		return handlers;
	}
}
