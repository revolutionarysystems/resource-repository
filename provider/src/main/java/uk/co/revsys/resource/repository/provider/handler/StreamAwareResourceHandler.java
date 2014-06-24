package uk.co.revsys.resource.repository.provider.handler;

import java.io.IOException;

public interface StreamAwareResourceHandler extends ResourceHandler{

    public void newStream() throws IOException;
    
    public void endOfStream() throws IOException;
    
}
