/**
 * 
 */
package org.jbpm.spring;

import java.util.Date;

import org.jbpm.services.task.audit.impl.model.AuditTaskImpl;
import org.jbpm.services.task.audit.impl.model.TaskEventImpl;
import org.jbpm.services.task.lifecycle.listeners.TaskLifeCycleEventListener;
import org.jbpm.services.task.utils.ClassUtil;
import org.kie.api.task.TaskEvent;
import org.kie.api.task.model.Task;
import org.kie.internal.task.api.TaskContext;
import org.kie.internal.task.api.TaskPersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class JBPMTaskLifeCycleEventListener implements TaskLifeCycleEventListener {
	
	private static final Logger logger = LoggerFactory.getLogger(JBPMTaskLifeCycleEventListener.class);

    public JBPMTaskLifeCycleEventListener(boolean flag) {
    }

    @Override
    public void afterTaskStartedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.STARTED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId ));
        
// @TODO:     Update UserAuditTask to Lucene        

        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
    }

    @Override
    public void afterTaskActivatedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.ACTIVATED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        
// @TODO:     Update UserAuditTask to Lucene        
        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
        auditTaskImpl.setDescription(ti.getDescription());    
        persistenceContext.merge(auditTaskImpl);
    }

    @Override
    public void afterTaskClaimedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.CLAIMED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        //@TODO:      Remove  GroupAuditTask to Lucene

        //@TODO:      Create new   UserAuditTask to Lucene

        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
        auditTaskImpl.setDescription(ti.getDescription());    
        persistenceContext.merge(auditTaskImpl);
    }

    @Override
    public void afterTaskSkippedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.SKIPPED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        //@TODO:  Find the UserAuditTask in the lucene index

      
            //@TODO: If the UserAuditTask is in lucene remove it
          
        
        //@TODO: Create the History Audit Task Impl, store it in the DB and also into lucene
        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
        auditTaskImpl.setDescription(ti.getDescription());    
        persistenceContext.merge(auditTaskImpl);
        //@TODO:        There is also the possibility that a GroupAuditTask exist in Lucene..
        //        make sure that you remove it as well        
        
    }

    @Override
    public void afterTaskStoppedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.STOPPED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        
      
        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);

    }

    @Override
    public void afterTaskCompletedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.COMPLETED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));

        //@TODO:      Make sure that you find and remove the USerAuditTask from lucene once it is completed    
        //@TODO: Create a new HistoryAuditTask to keep track about the task. 
        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
    }

    @Override
    public void afterTaskFailedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.FAILED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        
        // Same as task skipped
        
        
        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
    }

    @Override
    public void afterTaskAddedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if(ti.getTaskData().getProcessId() != null){
            userId = ti.getTaskData().getProcessId();
        }else if(ti.getTaskData().getActualOwner() != null){
            userId = ti.getTaskData().getActualOwner().getId();
        }
        AuditTaskImpl auditTaskImpl = new AuditTaskImpl( ti.getId(),ti.getName(),  ti.getTaskData().getStatus().name(),
                                                                                ti.getTaskData().getActivationTime() ,
                                                                                (ti.getTaskData().getActualOwner() != null)?ti.getTaskData().getActualOwner().getId():"",
                                                                                ti.getDescription(), ti.getPriority(),
                                                                                (ti.getTaskData().getCreatedBy() != null)?ti.getTaskData().getCreatedBy().getId():"",
                                                                                ti.getTaskData().getCreatedOn(), 
                                                                                ti.getTaskData().getExpirationTime(), ti.getTaskData().getProcessInstanceId(), 
                                                                                ti.getTaskData().getProcessId(), ti.getTaskData().getProcessSessionId(),
                                                                                ti.getTaskData().getDeploymentId(),
                                                                                ti.getTaskData().getParentId());
        persistenceContext.persist(auditTaskImpl);
        //@TODO: Create User or Group Task for Lucene
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.ADDED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
   
    
    }

    @Override
    public void afterTaskExitedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.EXITED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        
        //@TODO: Same as skipped

        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());  
        auditTaskImpl.setActivationTime(ti.getTaskData().getActivationTime());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
        
    }

    @Override
    public void afterTaskReleasedEvent(TaskEvent event) {
        
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        //@TODO: Remove UserAuditTask and create a new GroupAuditTask for lucene  
        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());  
        auditTaskImpl.setActivationTime(ti.getTaskData().getActivationTime());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner("");
            
        persistenceContext.merge(auditTaskImpl);

    }

    @Override
    public void afterTaskResumedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.RESUMED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
       //@TODO: Update Lucene UserAudit Task

        
        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());  
        auditTaskImpl.setActivationTime(ti.getTaskData().getActivationTime());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
    }

    @Override
    public void afterTaskSuspendedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.SUSPENDED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        
        //@TODO: Update Lucene Audit Task

        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
          auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());  
        auditTaskImpl.setActivationTime(ti.getTaskData().getActivationTime());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
    }

    @Override
    public void afterTaskForwardedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.FORWARDED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        //@TODO: Update Lucene Audit Task

        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());  
        auditTaskImpl.setActivationTime(ti.getTaskData().getActivationTime());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
    }

    @Override
    public void afterTaskDelegatedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.DELEGATED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
        
        //@TODO: Do I need to remove the USerAuditTask and create a GroupAuditTask in lucene???

        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());  
        auditTaskImpl.setActivationTime(ti.getTaskData().getActivationTime());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
    }
    
    @Override
    public void afterTaskNominatedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.NOMINATED, userId, new Date()));
        //@TODO: Update Lucene Audit Task

        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());  
        auditTaskImpl.setActivationTime(ti.getTaskData().getActivationTime());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId);
            
        persistenceContext.merge(auditTaskImpl);
    }
    
    /*
     * helper methods - start
     */
    
    protected AuditTaskImpl getAuditTask(TaskEvent event, TaskPersistenceContext persistenceContext, Task ti) {
    	AuditTaskImpl auditTaskImpl = persistenceContext.queryWithParametersInTransaction("getAuditTaskById", true, 
				persistenceContext.addParametersToMap("taskId", ti.getId()),
				ClassUtil.<AuditTaskImpl>castClass(AuditTaskImpl.class));
        
        return auditTaskImpl;
    }

	/*
     * helper methods - end
     */
	
    @Override
    public void beforeTaskActivatedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskClaimedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskSkippedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskStartedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskStoppedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskCompletedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskFailedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskAddedEvent(TaskEvent event) {
        
    }

    @Override
    public void beforeTaskExitedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskReleasedEvent(TaskEvent event) {
        String userId = "";
        Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
        if (ti.getTaskData().getActualOwner() != null) {
            userId = ti.getTaskData().getActualOwner().getId();
        }
        persistenceContext.persist(new TaskEventImpl(ti.getId(), org.kie.internal.task.api.model.TaskEvent.TaskEventType.RELEASED, ti.getTaskData().getProcessInstanceId(), ti.getTaskData().getWorkItemId(), userId));
      
        AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());  
        auditTaskImpl.setActivationTime(ti.getTaskData().getActivationTime());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
        auditTaskImpl.setStatus(ti.getTaskData().getStatus().name());
        auditTaskImpl.setActualOwner(userId); 
        persistenceContext.merge(auditTaskImpl);
        
    }

    @Override
    public void beforeTaskResumedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskSuspendedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskForwardedEvent(TaskEvent event) {

    }

    @Override
    public void beforeTaskDelegatedEvent(TaskEvent event) {

    }
    
    @Override
    public void beforeTaskNominatedEvent(TaskEvent event) {

    }

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) 
			return true;
        if ( obj == null ) 
        	return false;
        if ( (obj instanceof JBPMTaskLifeCycleEventListener) ) 
        	return true;
        
        return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
        int result = 1;
        result = prime * result + this.getClass().getName().hashCode();
        
        return result;
	}

	@Override
	public void beforeTaskUpdatedEvent(TaskEvent event) {
		
		
	}

	@Override
	public void afterTaskUpdatedEvent(TaskEvent event) {
		Task ti = event.getTask();
        TaskPersistenceContext persistenceContext = ((TaskContext)event.getTaskContext()).getPersistenceContext();
		AuditTaskImpl auditTaskImpl = getAuditTask(event, persistenceContext, ti);
        if (auditTaskImpl == null) {
        	logger.warn("Unable find audit task entry for task id {} '{}', skipping audit task update", ti.getId(), ti.getName());
        	return;
        }
        auditTaskImpl.setDescription(ti.getDescription());
        auditTaskImpl.setName(ti.getName());
        auditTaskImpl.setPriority(ti.getPriority());
        auditTaskImpl.setDueDate(ti.getTaskData().getExpirationTime());
            
        persistenceContext.merge(auditTaskImpl);
	}
    
    

}
