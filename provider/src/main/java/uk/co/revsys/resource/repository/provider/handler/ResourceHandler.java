package uk.co.revsys.resource.repository.provider.handler;

import java.io.IOException;
import java.io.InputStream;
import uk.co.revsys.resource.repository.model.Resource;

public interface ResourceHandler {
   
    public void handle(String path, Resource resource, InputStream contents) throws IOException;
    
}
