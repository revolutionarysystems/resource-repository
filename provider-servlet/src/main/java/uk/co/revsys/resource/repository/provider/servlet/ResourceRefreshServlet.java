package uk.co.revsys.resource.repository.provider.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import uk.co.revsys.resource.repository.provider.ResourceProvider;

public class ResourceRefreshServlet extends HttpServlet{

    private ResourceProvider resourceProvider;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        resourceProvider = webApplicationContext.getBean(ResourceProvider.class);
        if(resourceProvider.getHandler() instanceof ServletContextAware){
            ((ServletContextAware)resourceProvider.getHandler()).setServletContext(getServletContext());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resourceProvider.refresh();
        resp.setContentType("text/plain");
        resp.getWriter().write("Resources refreshed successfully");
    }

}
