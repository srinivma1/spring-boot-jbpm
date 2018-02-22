/**
 * 
 */
package org.jbpm.spring;


import org.jbpm.runtime.manager.impl.SimpleRuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.spring.manager.SpringRuntimeManagerFactoryImpl;


/**
 * @author mahesh.srinivas
 *
 */
public class CustomRuntimeManagerFactoryBean extends SpringRuntimeManagerFactoryImpl {
	

	 private  JBPMRegisterableItemsFactory registerableItemsFactory;
	
	
	public JBPMRegisterableItemsFactory getRegisterableItemsFactory() {
		return registerableItemsFactory;
	}


	public void setRegisterableItemsFactory(JBPMRegisterableItemsFactory registerableItemsFactory) {
		this.registerableItemsFactory = registerableItemsFactory;
	}


	@Override
    protected void adjustEnvironment(final RuntimeEnvironment environment) {
        super.adjustEnvironment(environment);
        ((SimpleRuntimeEnvironment) environment).setRegisterableItemsFactory(registerableItemsFactory);
    }
}
