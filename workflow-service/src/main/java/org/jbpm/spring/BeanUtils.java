/**
 * 
 */
package org.jbpm.spring;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author mahesh.srinivas
 *
 */
@Service
public class BeanUtils implements ApplicationContextAware {

	
	private static ApplicationContext context;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
        context = applicationContext;
	}
	
	 public static <T> T getBean(Class<T> beanClass) {
	        return context.getBean(beanClass);
	    }

}
