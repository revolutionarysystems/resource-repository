package uk.co.revsys.resource.repository.provider.handler;

public interface StreamAwareResourceHandler extends ResourceHandler{

    public void newStream();
    
    public void endOfStream();
    
}
