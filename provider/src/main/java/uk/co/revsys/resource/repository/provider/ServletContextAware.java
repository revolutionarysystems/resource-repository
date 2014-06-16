package uk.co.revsys.resource.repository.provider;

import javax.servlet.ServletContext;

public interface ServletContextAware {
    
    public void setServletContext(ServletContext servletContext);
    
}
