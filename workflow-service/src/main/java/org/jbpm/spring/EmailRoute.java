package org.jbpm.spring;



import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessDefinition;
import org.kie.internal.query.QueryContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EmailRoute extends RouteBuilder {

	
	@Autowired
	private RuntimeDataService runtimeDataService;
	
	@Autowired
	private ProcessService processService;
	
	// Start Process
	@Override
	public void configure() throws Exception {
		
		//Trigger Process Instance through mail using subject line containing process name and version - Start
		from("imaps://imap.googlemail.com:993?username=xxx@gmail.com&password=xxx&searchTerm.subject=Approval Request").routeId("processRoute")
		

		.log("Received subject is ${header.subject}")
		.process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				
				String subject =(String) exchange.getIn().getHeader("subject");
				
				//Subject line is of following format:Approval Request - ProcessName:EmailNotification,Version:1.0
				String subjects[] = subject.split("-");
				String processDetails = subjects[1].trim();
				String processArray[] = processDetails.split(",");
				String processNameDetails = processArray[0].trim();
				//TODO: Process Version not working. Needs to be seen later.
				String processVersionDetails = processArray[1].trim();
				String processVersionDetailArray[] = processVersionDetails.split(":");
				String processVersion = processVersionDetailArray[1].trim();
				String processNameDetailArray[] = processNameDetails.split(":");
			
			
				String processName = processNameDetailArray[1].trim();
		
				Collection<ProcessDefinition> processDefinitions = runtimeDataService.getProcesses(new QueryContext(0, 100));
				for(ProcessDefinition processDefinition:processDefinitions) {
					if(processName.equals(processDefinition.getName()) && processDefinition.getDeploymentId().contains(processVersion)){
						String deploymentId = processDefinition.getDeploymentId();
						String processId = processDefinition.getId();
						Map<String, Object> requestParams = new HashMap<String,Object>();
						requestParams.put("sender", exchange.getIn().getHeader("from"));
						requestParams.put("reviewer",exchange.getIn().getHeader("from"));
						
						long processInstanceId = processService.startProcess(deploymentId, processId,requestParams);
						System.out.println("Process Instance Id is"+processInstanceId);
						break;
					}
				}
			}
		});
		//Trigger Process Instance through mail using subject line containing process name and version - End
		
		
	
	}

	
	
}
