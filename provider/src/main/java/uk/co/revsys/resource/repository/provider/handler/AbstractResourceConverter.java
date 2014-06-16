package uk.co.revsys.resource.repository.provider.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import uk.co.revsys.resource.repository.model.Resource;

public abstract class AbstractResourceConverter<I extends Object> implements ResourceConverter<List<I>>, StreamAwareResourceHandler {

    private List<I> result = new LinkedList<I>();

    @Override
    public void handle(String path, Resource resource, InputStream contents) throws IOException {
        result.add(convert(contents));
    }
    
    public abstract I convert(InputStream contents) throws IOException;
    
    @Override
    public List<I> getResult() {
        return result;
    }

    @Override
    public void newStream() {
        result.clear();
    }

    @Override
    public void endOfStream() {
        
    }

}
