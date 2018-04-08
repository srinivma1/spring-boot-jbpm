package org.jbpm.spring;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.kie.api.event.process.ProcessEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class JBPMProcessEventListener implements ProcessEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(JBPMProcessEventListener.class);
	
	private ProducerTemplate producerTemplate = BeanUtils.getBean(ProducerTemplate.class);
	
	@Override
	public void afterNodeLeft(
			org.kie.api.event.process.ProcessNodeLeftEvent event) {
		LOGGER.info("AfterNodeLeft Event");
	}

	@Override
	public void afterNodeTriggered(
			org.kie.api.event.process.ProcessNodeTriggeredEvent event) {
		
		
		LOGGER.info("AfterNodeTriggered Event");
	}

	@Override
	public void afterProcessCompleted(
			org.kie.api.event.process.ProcessCompletedEvent event) {
		LOGGER.info("AfterProcessCompleted Event");
	}

	@Override
	public void afterProcessStarted(
			org.kie.api.event.process.ProcessStartedEvent event) {
		LOGGER.info("AfterProcessStarted Event");
		
	}

	@Override
	public void afterVariableChanged(
			org.kie.api.event.process.ProcessVariableChangedEvent event) {
		LOGGER.info("AfterVariableChanged Event");
	}

	@Override
	public void beforeNodeLeft(
			org.kie.api.event.process.ProcessNodeLeftEvent event) {
		LOGGER.info("BeforeNodeLeft Event");
	}

	@Override
	public void beforeNodeTriggered(
			org.kie.api.event.process.ProcessNodeTriggeredEvent event) {
		LOGGER.info("Before Node triggered. {} ", event.getNodeInstance().getNodeName());
		
		//producerTemplate.sendBodyAndHeaders("direct:emailApproval", message,headers);
		
	}

	@Override
	public void beforeProcessCompleted(
			org.kie.api.event.process.ProcessCompletedEvent event) {
		LOGGER.info("BeforeProcessCompleted Event");
	}

	@Override
	public void beforeProcessStarted(
			org.kie.api.event.process.ProcessStartedEvent event) {
		LOGGER.info("BeforeProcessStarted Event");
	}

	@Override
	public void beforeVariableChanged(
			org.kie.api.event.process.ProcessVariableChangedEvent event) {
		LOGGER.info("BeforeVariableChanged Event");
	}
}