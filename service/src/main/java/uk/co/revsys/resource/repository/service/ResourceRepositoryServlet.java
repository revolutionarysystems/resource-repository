package uk.co.revsys.resource.repository.service;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.ClasspathTemplateLoader;
import uk.co.revsys.resource.repository.ResourceRepository;
import uk.co.revsys.resource.repository.model.Directory;
import uk.co.revsys.resource.repository.model.Resource;
import de.neuland.jade4j.template.FileTemplateLoader;
import de.neuland.jade4j.template.JadeTemplate;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ResourceRepositoryServlet extends HttpServlet {

    private ResourceRepository resourceRepository;
    private JadeConfiguration jadeConfig;
    private DiskFileItemFactory factory;
    private ServletFileUpload upload;

    @Override
    public void init() throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        this.resourceRepository = webApplicationContext.getBean(ResourceRepository.class);
        //this.viewResolver = webApplicationContext.getBean(JadeViewResolver.class);
        jadeConfig = new JadeConfiguration();
        jadeConfig.setTemplateLoader(new ClasspathTemplateLoader());
        jadeConfig.setCaching(false);
        jadeConfig.setPrettyPrint(false);
        this.factory = new DiskFileItemFactory();
        this.upload = new ServletFileUpload(factory);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setHeader("Access-Control-Allow-Origin", "*");
            String filePath = req.getRequestURI().substring((req.getContextPath() + req.getServletPath()).length() + 1);
            if (filePath.isEmpty() || filePath.endsWith("/")) {
                List<Directory> directories = resourceRepository.listDirectories(filePath);
                List<Resource> resources = resourceRepository.listResources(filePath);
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("directories", directories);
                model.put("resources", resources);
                String viewType = req.getParameter("view");
                if (viewType != null && viewType.equals("json")) {
                    resp.getWriter().write(new JSONObject(model).toString());
                    resp.setContentType(MediaType.APPLICATION_JSON.toString());
                } else {
                    String parentPath = filePath;
                    if (!parentPath.isEmpty()) {
                        parentPath = parentPath.substring(0, parentPath.length() - 1);
                        parentPath = req.getContextPath() + "/" + parentPath.substring(0, parentPath.lastIndexOf("/") + 1);
                    } else {
                        parentPath = req.getContextPath() + "/";
                    }
                    model.put("parentPath", parentPath);
                    JadeTemplate template = jadeConfig.getTemplate("views/resources.jade");
                    jadeConfig.renderTemplate(template, model, resp.getWriter());
                }
            } else {
                String resourcePath = filePath.substring(0, filePath.lastIndexOf("/"));
                String resourceName = filePath.substring(filePath.lastIndexOf("/") + 1);
                InputStream resourceStream = resourceRepository.read(new Resource(resourcePath, resourceName));
                IOUtils.copy(resourceStream, resp.getOutputStream());
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            resp.sendError(404);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setHeader("Access-Control-Allow-Origin", "*");
            String filePath = req.getRequestURI().substring((req.getContextPath() + req.getServletPath()).length() + 1);
            List<FileItem> items = upload.parseRequest(req);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String resourcePath;
                    String resourceName;
                    if (filePath.endsWith("/")) {
                        resourcePath = filePath.substring(0, filePath.lastIndexOf("/"));
                        resourceName = item.getName();
                    } else {
                        resourcePath = filePath.substring(0, filePath.lastIndexOf("/"));
                        resourceName = filePath.substring(filePath.lastIndexOf("/") + 1);
                    }
                    Resource resource = new Resource(resourcePath, resourceName);
                    InputStream uploadStream = item.getInputStream();
                    resourceRepository.write(resource, uploadStream);
                    uploadStream.close();
                    resp.getWriter().write(resource.getName() + " uploaded successfully");
                }
            }
        } catch (FileUploadException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doDelete");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        String filePath = req.getRequestURI().substring((req.getContextPath() + req.getServletPath()).length() + 1);
        if (filePath.endsWith("/")) {
            filePath = filePath.substring(0, filePath.length()-1);
            Directory directory;
            if(filePath.contains("/")){
                String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
                String dirName = filePath.substring(filePath.lastIndexOf("/")+1);
                directory = new Directory(dirPath, dirName);
            }else{
                directory = new Directory(filePath);
            }
            resourceRepository.delete(directory);
            resp.getWriter().write(directory.getName() + " deleted successfully");
        } else {
            String resourcePath = filePath.substring(0, filePath.lastIndexOf("/"));
            String resourceName = filePath.substring(filePath.lastIndexOf("/") + 1);
            resourceRepository.delete(new Resource(resourcePath, resourceName));
            resp.getWriter().write(resourceName + " deleted successfully");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
        super.doOptions(req, resp);
    }

}
