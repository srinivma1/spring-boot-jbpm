package org.jbpm.spring;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.bpmn2.handler.WorkItemHandlerRuntimeException;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;



@Component
public class JBPMServiceTaskHandler implements WorkItemHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(JBPMServiceTaskHandler.class);
	
	@Autowired
	private ApplicationContext context;

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager 	manager) {
	        
		logger.info("Entered into service task name:" + workItem.getName());
		
        String service = (String) workItem.getParameter(JBPMConstants.INTERFACE);
        //service is the class name, ex: SendNotificationService
        String interfaceImplementationRef = (String) workItem.getParameter(JBPMConstants.INTERFACE_IMPLEMENTATION_REF); 
        //interfaceImplementationRef is  the FQN of class, ex: com.toyota.tme.etooling.jbpm.tlc.SendNotificationService
        String operation = (String) workItem.getParameter(JBPMConstants.OPERATION);
        //operation is the method name, ex: execute
        String parameterType = (String) workItem.getParameter(JBPMConstants.PARAMETER_TYPE);
        //parameterType is the argument data type for Operation
        Object parameter = workItem.getParameter(JBPMConstants.PARAMETER);
        //parameter is the argument value for Operation
        String parameterName = (String) workItem.getParameter(JBPMConstants.PARAMETER_NAME);
        //parameterName is the argument name for Operation
        
        try {
	        String beanId = interfaceImplementationRef.substring(interfaceImplementationRef.lastIndexOf(".") + 1);
	        JBPMServiceTaskExecutor serviceTaskExecutor = context.getBean(beanId, JBPMServiceTaskExecutor.class);
	        
	        Map<String, Object> parameterMap = new HashMap<String, Object>();
	        parameterMap.put(parameterName, parameter);
	        parameterMap.put(JBPMConstants.PROCESS_INSTANCE_ID, workItem.getProcessInstanceId());
	        
	        Map<String, Object> results = serviceTaskExecutor.execute(parameterMap);
	        manager.completeWorkItem(workItem.getId(), results);
	        
        } catch (Exception ex) {
        	handleException(ex, service, interfaceImplementationRef, operation, parameterType, parameter);
        } 
	}
	
	private void handleException(Throwable cause, String service, String interfaceImplementationRef, String operation, String paramType, Object param) { 
        logger.debug("Handling exception " + cause.getMessage() + " inside service " + service + " or " + interfaceImplementationRef + 
        		" and operation " + operation + " with param type " + paramType + " and value " + param);
        
        WorkItemHandlerRuntimeException  wihRe = new WorkItemHandlerRuntimeException(cause);
        wihRe.setStackTrace(cause.getStackTrace());
        wihRe.setInformation(JBPMConstants.INTERFACE, service);
        wihRe.setInformation(JBPMConstants.INTERFACE_IMPLEMENTATION_REF, interfaceImplementationRef);
        wihRe.setInformation(JBPMConstants.OPERATION, operation);
        wihRe.setInformation(JBPMConstants.PARAMETER_TYPE, paramType);
        wihRe.setInformation(JBPMConstants.PARAMETER, param);
        wihRe.setInformation(WorkItemHandlerRuntimeException.WORKITEMHANDLERTYPE, this.getClass().getSimpleName());
        
        throw wihRe;
    }
}
