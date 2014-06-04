package uk.co.revsys.resource.repository.provider;

import java.io.IOException;
import java.io.InputStream;
import uk.co.revsys.resource.repository.model.Resource;

public interface ResourceHandler {
   
    public void handle(Resource resource, InputStream contents) throws IOException;
    
    public boolean canHandle(Resource resource);
    
}
